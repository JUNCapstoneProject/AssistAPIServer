package com.help.stockassistplatform.domain.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.help.stockassistplatform.domain.stock.dto.StockSearchResponse;
import com.help.stockassistplatform.domain.stock.repository.StockViewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {
	private final StockViewRepository stockViewRepository;

	public List<StockSearchResponse> search(final String query) {
		return stockViewRepository
			.searchPreview(query)
			.stream()
			.map(StockSearchResponse::from)
			.toList();
	}
}
