package com.help.stockassistplatform.domain.stock.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

public record StockPriceTimeSeriesResponse(
	List<TimeSeriesData> data,
	String interval
) {
	public record TimeSeriesData(
		LocalDate date,
		BigDecimal price
	) {
	}
}
