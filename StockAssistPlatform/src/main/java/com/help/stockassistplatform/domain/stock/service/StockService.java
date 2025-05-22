package com.help.stockassistplatform.domain.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.financial.entity.StockPriceView;
import com.help.stockassistplatform.domain.financial.repository.StockPriceViewRepository;
import com.help.stockassistplatform.domain.stock.dto.StockAnalysisResponse;
import com.help.stockassistplatform.domain.stock.dto.StockSearchResponse;
import com.help.stockassistplatform.domain.stock.dto.response.SliceResponse;
import com.help.stockassistplatform.domain.stock.dto.response.StockSummaryDto;
import com.help.stockassistplatform.domain.stock.indexed.entity.CompanyIndexed;
import com.help.stockassistplatform.domain.stock.indexed.repository.CompanyIndexedRepository;
import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class StockService {
	private final CompanyIndexedRepository companyViewRepository;
	private final AiAnalysisOrchestrator aiAnalysisOrchestrator;
	private final StockPriceViewRepository stockPriceViewRepository;

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

	public SliceResponse<StockSummaryDto> getStocksByMarketCap(final int page, final int pageSize) {
		final int groupSize = 10;

		final int groupIndex = (page - 1) / groupSize;
		final int groupOffset = groupIndex * groupSize * pageSize;
		final int fetchLimit = groupSize * pageSize + 1;

		final List<StockPriceView> fetched = stockPriceViewRepository.findByMarketCapPaged(fetchLimit, groupOffset);

		final boolean hasNext = fetched.size() > groupSize * pageSize;
		final int total = Math.min(fetched.size(), groupSize * pageSize);

		final int indexInGroup = (page - 1) % groupSize;
		final int fromIndex = indexInGroup * pageSize;
		final int toIndex = Math.min(fromIndex + pageSize, total);

		if (fromIndex >= toIndex) {
			throw new CustomException(ErrorCode.PAGE_OUT_OF_RANGE);
		}

		final List<StockSummaryDto> data = fetched.subList(fromIndex, toIndex)
			.stream()
			.map(StockSummaryDto::from)
			.toList();

		return new SliceResponse<>(data, total, hasNext);
	}
}
