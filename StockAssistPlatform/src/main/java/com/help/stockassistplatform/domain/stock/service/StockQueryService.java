package com.help.stockassistplatform.domain.stock.service;

import java.time.LocalDate;
import java.util.List;

import com.help.stockassistplatform.domain.stock.dto.response.StockPriceTimeSeriesResponse;
import com.help.stockassistplatform.domain.stock.dto.response.StockSummaryResponse;

public interface StockQueryService {

	/**
	 * 다수 종목의 요약 정보를 조회합니다.
	 *
	 * @param symbols 종목 코드 리스트
	 * @return 종목 요약 응답 DTO
	 */
	StockSummaryResponse getStockSummaries(List<String> symbols);

	/**
	 * 특정 종목의 시계열 가격 데이터를 조회합니다.
	 *
	 * @param symbol 조회할 종목 코드
	 * @param start 시작 날짜
	 * @param end 종료 날짜
	 * @param period 조회 기간 (1M, 6M, YTD 등)
	 * @return 시계열 가격 응답 DTO
	 */
	StockPriceTimeSeriesResponse getPriceTimeSeries(String symbol, LocalDate start, LocalDate end, String period);
}
