package com.help.stockassistplatform.domain.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.help.stockassistplatform.domain.stock.dto.StockSearchResponse;
import com.help.stockassistplatform.domain.stock.indexed.repository.CompanyIndexedRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class StockService {
	private final CompanyIndexedRepository companyViewRepository;

	public List<StockSearchResponse> search(final String query) {
		return companyViewRepository
			.searchPreview(query)
			.stream()
			.map(StockSearchResponse::from)
			.toList();
	}
}
