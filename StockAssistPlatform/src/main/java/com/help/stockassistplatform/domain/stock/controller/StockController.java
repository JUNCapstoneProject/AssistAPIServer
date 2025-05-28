package com.help.stockassistplatform.domain.stock.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.domain.stock.dto.request.StockAnalysisType;
import com.help.stockassistplatform.domain.stock.dto.response.SliceResponse;
import com.help.stockassistplatform.domain.stock.dto.response.StockAnalysisResponse;
import com.help.stockassistplatform.domain.stock.dto.response.StockPriceTimeSeriesResponse;
import com.help.stockassistplatform.domain.stock.dto.response.StockSearchResponse;
import com.help.stockassistplatform.domain.stock.dto.response.StockSummaryDto;
import com.help.stockassistplatform.domain.stock.dto.response.StockSummaryResponse;
import com.help.stockassistplatform.domain.stock.service.StockQueryService;
import com.help.stockassistplatform.domain.stock.service.StockService;
import com.help.stockassistplatform.global.common.response.ApiResponse;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/stocks")
@RequiredArgsConstructor
public class StockController {
	private final StockService stockService;
	private final StockQueryService stockQueryService;

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

	@GetMapping("/summary")
	public ApiResponse<?> getSummary(@RequestParam final List<String> symbols) {
		final StockSummaryResponse response = stockQueryService.getStockSummaries(symbols);
		return ApiResponse.success(response);
	}

	@GetMapping("/prices")
	public ApiResponse<?> getPriceTimeSeries(
		@RequestParam final String symbols,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
		@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end,
		@RequestParam String period) {

		final StockPriceTimeSeriesResponse response = stockQueryService.getPriceTimeSeries(symbols, start, end, period);
		return ApiResponse.success(response);
	}
}
