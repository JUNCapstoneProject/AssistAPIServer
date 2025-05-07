package com.help.stockassistplatform.domain.stock.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.domain.stock.dto.StockAnalysisResponse;
import com.help.stockassistplatform.domain.stock.dto.StockSearchResponse;
import com.help.stockassistplatform.domain.stock.service.StockService;
import com.help.stockassistplatform.global.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {
	private final StockService stockService;

	@GetMapping("/search")
	public ApiResponse<?> searchCompanies(
		@RequestParam(value = "query", required = false, defaultValue = "") final String query
	) {
		if (query.isBlank()) {
			return ApiResponse.success(Map.of("searchData", List.of()));
		}
		final List<StockSearchResponse> results = stockService.search(query);
		return ApiResponse.success(Map.of("searchData", results));
	}

	@GetMapping("/analysis")
	public ApiResponse<?> getStockAnalysis() {
		final List<StockAnalysisResponse> results = stockService.getStockAnalysis();
		return ApiResponse.success(results);
	}
}
