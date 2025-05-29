package com.help.stockassistplatform.domain.stock.dto.response;

import java.util.List;
import java.util.Map;

public record StockPriceTimeSeriesResponse(
	List<Map<String, Object>> data,
	String interval
) {
}
