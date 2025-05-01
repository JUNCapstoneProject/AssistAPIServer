package com.help.stockassistplatform.domain.financial.service.impl;

import com.help.stockassistplatform.domain.financial.dto.response.*;
import com.help.stockassistplatform.domain.financial.entity.*;
import com.help.stockassistplatform.domain.financial.repository.*;
import com.help.stockassistplatform.domain.financial.service.*;
import com.help.stockassistplatform.domain.financial.mapper.FinancialMapper;
import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

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

    @Override
    public FinancialDetailResponse getDetailByTicker(String ticker) {
        StockPriceView price = stockPriceViewRepository.findTop1ByTickerOrderByPostedAtDesc(ticker)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKER_NOT_FOUND));

        String name = price.getName();
        BigDecimal close = price.getClose();
        Float change = price.getChange();

        String status = analysisRepository.findLatestByCompanyOrderByPostedAtDesc(ticker)
                .map(FinancialAnalysisView::getAiAnalysis)
                .orElse("중립");

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
        String lastTicker = null;
        int currentIndex = 0;
        int startIndex = (page - 1) * PAGE_SIZE;

        List<FinancialDetailResponse> financials = new ArrayList<>();
        boolean hasNext = false;

        outer:
        while (true) {
            List<StockPriceView> slice = stockPriceViewRepository.findNextGroupedByTicker(lastTicker, PAGE_SIZE);
            if (slice.isEmpty()) break;

            lastTicker = slice.get(slice.size() - 1).getTicker();
            List<String> tickers = slice.stream().map(StockPriceView::getTicker).toList();

            Map<String, FinancialAnalysisView> analysisMap = analysisRepository.findLatestByTickers(tickers).stream()
                    .collect(Collectors.toMap(FinancialAnalysisView::getCompany, v -> v));

            Map<String, List<IncomeStatementView>> incomeMap = incomeRepository.findRecent2ByTickers(tickers).stream()
                    .collect(Collectors.groupingBy(IncomeStatementView::getCompany));
            Map<String, List<BalanceSheetView>> balanceMap = balanceRepository.findRecent2ByTickers(tickers).stream()
                    .collect(Collectors.groupingBy(BalanceSheetView::getCompany));
            Map<String, List<CashFlowView>> cashMap = cashRepository.findRecent2ByTickers(tickers).stream()
                    .collect(Collectors.groupingBy(CashFlowView::getCompany));
            Map<String, List<FinancialRatioView>> ratioMap = ratioRepository.findRecent2ByTickers(tickers).stream()
                    .collect(Collectors.groupingBy(FinancialRatioView::getCompany));

            for (StockPriceView stock : slice) {
                String ticker = stock.getTicker();

                if (!incomeMap.containsKey(ticker) || !balanceMap.containsKey(ticker)
                        || !cashMap.containsKey(ticker) || !ratioMap.containsKey(ticker)) {
                    continue;
                }

                if (currentIndex++ < startIndex) continue;

                financials.add(FinancialDetailResponse.from(
                        stock.getName(),
                        ticker,
                        stock.getClose(),
                        stock.getChange(),
                        Optional.ofNullable(analysisMap.get(ticker)).map(FinancialAnalysisView::getAiAnalysis).orElse("중립"),
                        FinancialMapper.mapIncome(incomeMap.get(ticker)),
                        FinancialMapper.mapBalance(balanceMap.get(ticker)),
                        FinancialMapper.mapCash(cashMap.get(ticker)),
                        FinancialMapper.mapRatio(ratioMap.get(ticker))
                ));

                if (financials.size() == PAGE_SIZE) {
                    hasNext = true;  // 더 가져올 수 있는 ticker가 있을 가능성
                    break outer;
                }
            }
        }

        if (financials.isEmpty()) {
            throw new CustomException(ErrorCode.NOT_FOUND); // 페이지를 찾을 수 없습니다
        }

        return FinancialListResponse.from(financials, page, hasNext);
    }
}