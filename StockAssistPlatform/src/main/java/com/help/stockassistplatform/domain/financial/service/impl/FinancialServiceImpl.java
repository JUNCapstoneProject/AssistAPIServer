package com.help.stockassistplatform.domain.financial.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.Objects;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.context.event.EventListener;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.financial.dto.response.FinancialDetailResponse;
import com.help.stockassistplatform.domain.financial.dto.response.FinancialItemResponse;
import com.help.stockassistplatform.domain.financial.dto.response.FinancialListResponse;
import com.help.stockassistplatform.domain.financial.entity.BalanceSheetView;
import com.help.stockassistplatform.domain.financial.entity.CashFlowView;
import com.help.stockassistplatform.domain.financial.entity.FinancialAnalysisView;
import com.help.stockassistplatform.domain.financial.entity.FinancialRatioView;
import com.help.stockassistplatform.domain.financial.entity.IncomeStatementView;
import com.help.stockassistplatform.domain.financial.entity.StockPriceView;
import com.help.stockassistplatform.domain.financial.mapper.FinancialMapper;
import com.help.stockassistplatform.domain.financial.repository.BalanceSheetViewRepository;
import com.help.stockassistplatform.domain.financial.repository.CashFlowViewRepository;
import com.help.stockassistplatform.domain.financial.repository.FinancialAnalysisViewRepository;
import com.help.stockassistplatform.domain.financial.repository.FinancialRatioViewRepository;
import com.help.stockassistplatform.domain.financial.repository.IncomeStatementViewRepository;
import com.help.stockassistplatform.domain.financial.repository.StockPriceViewRepository;
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

	@Scheduled(fixedDelay = 1000 * 60 * 2) // 2분마다 price 갱신
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
				.map(ticker -> {
					StockPriceView stock = priceMap.get(ticker);
					if (stock == null || !incomeMap.containsKey(ticker) || !balanceMap.containsKey(ticker)
							|| !cashMap.containsKey(ticker) || !ratioMap.containsKey(ticker)) return null;

					return FinancialDetailResponse.from(
							stock.getName(), ticker, stock.getPrice(), stock.getChange(),
							Optional.ofNullable(analysisMap.get(ticker)).map(FinancialAnalysisView::getAiAnalysis).orElse(null),
							FinancialMapper.mapIncome(incomeMap.get(ticker)),
							FinancialMapper.mapBalance(balanceMap.get(ticker)),
							FinancialMapper.mapCash(cashMap.get(ticker)),
							FinancialMapper.mapRatio(ratioMap.get(ticker))
					);
				})
				.filter(Objects::nonNull)
				.toList();

		boolean hasNext = offset + pageSize < cachedTickerList.size();

		if (financials.isEmpty()) {
			throw new CustomException(ErrorCode.NOT_FOUND);
		}

		return FinancialListResponse.from(financials, page, hasNext);
	}

	private void initializeStaticCache() {
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
