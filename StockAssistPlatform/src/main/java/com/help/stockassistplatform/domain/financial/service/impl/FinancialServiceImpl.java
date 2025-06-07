package com.help.stockassistplatform.domain.financial.service.impl;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.financial.dto.response.FinancialDetailResponse;
import com.help.stockassistplatform.domain.financial.dto.response.FinancialItemResponse;
import com.help.stockassistplatform.domain.financial.dto.response.FinancialListResponse;
import com.help.stockassistplatform.domain.financial.entity.*;
import com.help.stockassistplatform.domain.financial.mapper.FinancialMapper;
import com.help.stockassistplatform.domain.financial.repository.*;
import com.help.stockassistplatform.domain.financial.service.FinancialService;
import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FinancialServiceImpl implements FinancialService {

    private final StockPriceViewRepository stockPriceViewRepository;
    private final FinancialAnalysisViewRepository analysisRepository;
    private final IncomeStatementViewRepository incomeRepository;
    private final BalanceSheetViewRepository balanceRepository;
    private final CashFlowViewRepository cashRepository;
    private final FinancialRatioViewRepository ratioRepository;

    private static final int PAGE_SIZE = 3;

    private volatile boolean cacheInitialized = false;
    private List<String> cachedTickerList;
    private Map<String, StockPriceView> priceMap;
    private Map<String, FinancialAnalysisView> analysisMap;
    private Map<String, List<IncomeStatementView>> incomeMap;
    private Map<String, List<BalanceSheetView>> balanceMap;
    private Map<String, List<CashFlowView>> cashMap;
    private Map<String, List<FinancialRatioView>> ratioMap;

    @EventListener(ApplicationReadyEvent.class)
    public void preloadCacheOnStartup() {
        initializeStaticCache();
        refreshPriceCache();
        cacheInitialized = true;
    }

     /**
     * 주가 관련 데이터는 빈번히 변경되므로,
     * 2분마다 최신 가격 정보를 조회하여 캐시에 갱신합니다.
     */
    
    @Scheduled(fixedDelay = 1000 * 60 * 2)
    public void refreshPriceCache() {
        if (cachedTickerList == null || cachedTickerList.isEmpty()) return;
        this.priceMap = stockPriceViewRepository.findByTickerIn(cachedTickerList).stream()
                .collect(Collectors.toMap(StockPriceView::getTicker, s -> s));
    }

    @Override
    public FinancialDetailResponse getDetailByTicker(String ticker) {
        StockPriceView price = stockPriceViewRepository.findOneByTicker(ticker)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKER_NOT_FOUND));

        String name = price.getName();
        BigDecimal close = price.getPrice();
        Float change = price.getChange();

        Integer status = analysisRepository.findLatestByCompanyOrderByPostedAtDesc(ticker)
                .map(FinancialAnalysisView::getAiAnalysis)
                .orElse(null);

        List<FinancialItemResponse> incomeList = FinancialMapper.mapIncome(
                incomeRepository.findRecent2ByCompanyOrderByPostedAtDesc(ticker));
        List<FinancialItemResponse> balanceList = FinancialMapper.mapBalance(
                balanceRepository.findRecent2ByCompanyOrderByPostedAtDesc(ticker));
        List<FinancialItemResponse> cashList = FinancialMapper.mapCash(
                cashRepository.findRecent2ByCompanyOrderByPostedAtDesc(ticker));
        List<FinancialItemResponse> ratioList = FinancialMapper.mapRatio(
                ratioRepository.findRecent2ByCompanyOrderByPostedAtDesc(ticker));

        return FinancialDetailResponse.from(
                name, ticker, close, change, status,
                incomeList, balanceList, cashList, ratioList
        );
    }

    @Override
    public FinancialListResponse getListByPage(int page) {
        if (!cacheInitialized) {
            synchronized (this) {
                if (!cacheInitialized) {
                    initializeStaticCache();
                    refreshPriceCache();
                    cacheInitialized = true;
                }
            }
        }

        int pageSize = PAGE_SIZE;
        int offset = (page - 1) * pageSize;

        if (offset >= cachedTickerList.size()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        List<String> tickers = cachedTickerList.subList(offset, Math.min(offset + pageSize, cachedTickerList.size()));

        List<FinancialDetailResponse> financials = tickers.stream()
                .map(this::mapToDetail)
                .filter(Objects::nonNull)
                .toList();

        boolean hasNext = offset + pageSize < cachedTickerList.size();

        if (financials.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        return FinancialListResponse.from(financials, page, hasNext);
    }

    @Override
    public FinancialListResponse getList(int page, int size, String sortBy, String sort, Integer sentiment) {
        if (!cacheInitialized) {
            synchronized (this) {
                if (!cacheInitialized) {
                    initializeStaticCache();
                    refreshPriceCache();
                    cacheInitialized = true;
                }
            }
        }

        List<String> filteredTickers = cachedTickerList.stream()
                .filter(ticker -> {
                    if (sentiment == null) return true;
                    FinancialAnalysisView analysis = analysisMap.get(ticker);
                    return analysis != null && sentiment.equals(analysis.getAiAnalysis());
                })
                .collect(Collectors.toList());

        Comparator<String> comparator = Comparator.comparing(ticker -> {
            if ("revenue".equalsIgnoreCase(sortBy)) {
                List<IncomeStatementView> incomeList = incomeMap.get(ticker);
                if (incomeList != null && !incomeList.isEmpty()) {
                    BigDecimal revenue = incomeList.get(0).getTotalRevenue();
                    return revenue != null ? revenue : BigDecimal.ZERO;
                }
                return BigDecimal.ZERO;
            }
            StockPriceView price = priceMap.get(ticker);
            return price != null ? price.getPrice() : BigDecimal.ZERO;
        });

        if ("desc".equalsIgnoreCase(sort)) {
            comparator = comparator.reversed();
        }
        filteredTickers.sort(comparator);

        int offset = (page - 1) * size;
        int end = Math.min(offset + size, filteredTickers.size());

        if (offset >= filteredTickers.size()) {
            throw new CustomException(ErrorCode.NOT_FOUND);
        }

        List<String> pagedTickers = filteredTickers.subList(offset, end);

        List<FinancialDetailResponse> financials = pagedTickers.stream()
                .map(this::mapToDetail)
                .filter(Objects::nonNull)
                .toList();

        boolean hasNext = end < filteredTickers.size();

        return FinancialListResponse.from(financials, page, hasNext);
    }

    private FinancialDetailResponse mapToDetail(String ticker) {
        StockPriceView stock = priceMap.get(ticker);
        if (stock == null || !incomeMap.containsKey(ticker) || !balanceMap.containsKey(ticker)
                || !cashMap.containsKey(ticker) || !ratioMap.containsKey(ticker))
            return null;

        return FinancialDetailResponse.from(
                stock.getName(), ticker, stock.getPrice(), stock.getChange(),
                Optional.ofNullable(analysisMap.get(ticker)).map(FinancialAnalysisView::getAiAnalysis).orElse(null),
                FinancialMapper.mapIncome(incomeMap.get(ticker)),
                FinancialMapper.mapBalance(balanceMap.get(ticker)),
                FinancialMapper.mapCash(cashMap.get(ticker)),
                FinancialMapper.mapRatio(ratioMap.get(ticker))
        );
    }

    @Scheduled(fixedDelay = 1000 * 60 * 60 * 2)
    protected void initializeStaticCache() {
        this.cachedTickerList = stockPriceViewRepository.findAllTickersSorted();

        this.analysisMap = analysisRepository.findLatestByTickers(cachedTickerList).stream()
                .collect(Collectors.toMap(FinancialAnalysisView::getCompany, a -> a));

        this.incomeMap = incomeRepository.findRecent2ByTickers(cachedTickerList).stream()
                .collect(Collectors.groupingBy(IncomeStatementView::getCompany));

        this.balanceMap = balanceRepository.findRecent2ByTickers(cachedTickerList).stream()
                .collect(Collectors.groupingBy(BalanceSheetView::getCompany));

        this.cashMap = cashRepository.findRecent2ByTickers(cachedTickerList).stream()
                .collect(Collectors.groupingBy(CashFlowView::getCompany));

        this.ratioMap = ratioRepository.findRecent2ByTickers(cachedTickerList).stream()
                .collect(Collectors.groupingBy(FinancialRatioView::getCompany));
    }
}
