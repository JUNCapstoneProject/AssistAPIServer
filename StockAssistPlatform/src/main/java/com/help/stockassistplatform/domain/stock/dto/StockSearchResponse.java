package com.help.stockassistplatform.domain.stock.dto;

import com.help.stockassistplatform.domain.stock.entity.StockView;

public record StockSearchResponse(String ticker, String name) {
	public static StockSearchResponse from(final StockView stock) {
		return new StockSearchResponse(
			stock.getTicker(),
			stock.getCompanyNameKr()
		);
	}
}
