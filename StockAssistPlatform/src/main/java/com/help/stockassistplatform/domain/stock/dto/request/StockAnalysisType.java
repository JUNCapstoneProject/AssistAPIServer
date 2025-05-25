package com.help.stockassistplatform.domain.stock.dto.request;

public enum StockAnalysisType {
	FINANCIAL,
	NEWS;

	public static StockAnalysisType from(final String type) {
		if (null == type) {
			return FINANCIAL;
		}
		try {
			return StockAnalysisType.valueOf(type.toUpperCase());
		} catch (final IllegalArgumentException e) {
			return FINANCIAL;
		}
	}
}
