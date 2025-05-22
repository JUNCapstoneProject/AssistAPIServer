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

	public SliceResponse<StockSummaryDto> getStocksByMarketCap(final int page, final int size) {
		final int groupSize = 10;

		// page = 13 → groupIndex = 1 (11~20페이지)
		final int groupIndex = (page - 1) / groupSize;

		// 11과 20 사이의 페이지라면 startPage는 11, offset은 (11 - 1) * size = 100
		final int startPage = groupIndex * groupSize + 1;
		final int offset = (startPage - 1) * size;

		// (groupSize * size + 1) 만큼 가져와야 hasNext 판단 가능
		final int fetchLimit = groupSize * size + 1;

		List<StockPriceView> result = stockPriceViewRepository.findByMarketCapPaged(fetchLimit, offset);

		final boolean hasNext = result.size() > groupSize * size;
		if (hasNext) {
			result = result.subList(0, groupSize * size);
		}

		// 클라이언트가 요청한 페이지에 해당하는 데이터 추출
		final int indexInGroup = (page - 1) % groupSize;
		final int fromIndex = indexInGroup * size;
		final int toIndex = Math.min(fromIndex + size, result.size());

		final List<StockSummaryDto> data = result.subList(fromIndex, toIndex)
			.stream()
			.map(StockSummaryDto::from)
			.toList();

		return new SliceResponse<>(data, result.size(), hasNext);
	}
}
