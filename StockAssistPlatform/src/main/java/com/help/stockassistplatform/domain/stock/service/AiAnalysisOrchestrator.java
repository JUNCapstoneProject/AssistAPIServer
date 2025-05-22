package com.help.stockassistplatform.domain.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.financial.repository.FinancialAnalysisViewRepository;
import com.help.stockassistplatform.domain.financial.repository.StockPriceViewRepository;
import com.help.stockassistplatform.domain.stock.dto.StockAnalysisResponse;
import com.help.stockassistplatform.domain.stock.dto.StockTickerNameDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AiAnalysisOrchestrator {
	private final StockPriceViewRepository stockPriceViewRepository;
	private final FinancialAnalysisViewRepository financialAnalysisViewRepository;

	public List<StockAnalysisResponse> stockAnalysisResponses() {
		final List<StockTickerNameDto> top4TickersByMarketCap = getTop4TickerAndName();
		return getTop4StockAnalysis(top4TickersByMarketCap);
	}

	private List<StockTickerNameDto> getTop4TickerAndName() {
		return stockPriceViewRepository.findTop4TickerAndNameByMarketCap().stream()
			.map(row -> new StockTickerNameDto((String)row[0], (String)row[1]))
			.toList();
	}

	private List<StockAnalysisResponse> getTop4StockAnalysis(final List<StockTickerNameDto> stockInfos) {
		return stockInfos.stream()
			.map(tickerNameDto -> {
				final String ticker = tickerNameDto.ticker();
				final String name = tickerNameDto.name();

				final String aiAnalysis = financialAnalysisViewRepository
					.findLatestAiAnalysisByCompany(ticker)
					.orElse("분석 결과 없음");

				return new StockAnalysisResponse(ticker, name, aiAnalysis);
			})
			.toList();
	}
}
