package com.help.stockassistplatform.domain.stock.dto;

import com.help.stockassistplatform.domain.stock.indexed.entity.CompanyIndexed;

public record StockSearchResponse(
	String ticker,
	String nameKr,
	String nameEn
) {
	public static StockSearchResponse from(final CompanyIndexed stock) {
		return new StockSearchResponse(
			stock.getTicker(),
			stock.getCompanyNameKr(),
			stock.getCompanyNameEn()
		);
	}
}
