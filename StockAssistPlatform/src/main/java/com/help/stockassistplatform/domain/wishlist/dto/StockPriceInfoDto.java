package com.help.stockassistplatform.domain.wishlist.dto;

import java.math.BigDecimal;

import com.help.stockassistplatform.domain.financial.entity.StockPriceView;

public record StockPriceInfoDto(
	String ticker,
	String name,
	BigDecimal price
) {
	public static StockPriceInfoDto from(StockPriceView view) {
		return new StockPriceInfoDto(view.getTicker(), view.getName(), view.getPrice());
	}
}
