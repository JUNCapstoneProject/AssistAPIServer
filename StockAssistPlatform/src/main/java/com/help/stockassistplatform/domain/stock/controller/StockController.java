package com.help.stockassistplatform.domain.stock.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.domain.stock.dto.StockAnalysisResponse;
import com.help.stockassistplatform.domain.stock.dto.StockAnalysisType;
import com.help.stockassistplatform.domain.stock.dto.StockSearchResponse;
import com.help.stockassistplatform.domain.stock.dto.response.SliceResponse;
import com.help.stockassistplatform.domain.stock.dto.response.StockSummaryDto;
import com.help.stockassistplatform.domain.stock.service.StockService;
import com.help.stockassistplatform.global.common.response.ApiResponse;

import jakarta.validation.constraints.Min;
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
	public ApiResponse<?> getStockAnalysis(
		@RequestParam(value = "type", defaultValue = "financial") final String typeStr) {
		final StockAnalysisType type = StockAnalysisType.from(typeStr);
		final List<StockAnalysisResponse> results = switch (type) {
			case FINANCIAL -> stockService.getStockAnalysis();
			case NEWS -> stockService.getStockAnalysis();
		};
		return ApiResponse.success(results);
	}

	@GetMapping("/marketcap")
	public ApiResponse<SliceResponse<StockSummaryDto>> getStocksByMarketCap(
		@RequestParam(defaultValue = "1") @Min(1L) final int page,
		@RequestParam(defaultValue = "10") @Min(1L) final int size
	) {
		final SliceResponse<StockSummaryDto> response = stockService.getStocksByMarketCap(page, size);
		return ApiResponse.success(response);
	}
}
