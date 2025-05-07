package com.help.stockassistplatform.domain.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.stock.dto.StockAnalysisResponse;
import com.help.stockassistplatform.domain.stock.dto.StockSearchResponse;
import com.help.stockassistplatform.domain.stock.indexed.entity.CompanyIndexed;
import com.help.stockassistplatform.domain.stock.indexed.repository.CompanyIndexedRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StockService {
	private final CompanyIndexedRepository companyViewRepository;
	private final AiAnalysisOrchestrator aiAnalysisOrchestrator;

	public List<StockSearchResponse> search(final String query) {
		final List<CompanyIndexed> result;

		if (2 >= query.length()) {
			final String booleanQuery = query + "*";
			result = companyViewRepository.prefixSearch(booleanQuery);
		} else {
			result = companyViewRepository.relevanceSearch(query);
		}

		return result
			.stream()
			.map(StockSearchResponse::from)
			.toList();
	}

	public List<StockAnalysisResponse> getStockAnalysis() {
		return aiAnalysisOrchestrator.stockAnalysisResponses();
	}
}
