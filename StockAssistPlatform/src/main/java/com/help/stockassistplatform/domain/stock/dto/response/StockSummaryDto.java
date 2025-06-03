package com.help.stockassistplatform.domain.stock.dto.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.help.stockassistplatform.domain.financial.entity.StockPriceView;

public record StockSummaryDto(
	String name,
	String symbol,
	BigInteger marketCap,
	BigDecimal price,
	Float change,
	Long volume,
	BigDecimal pe,
	BigDecimal eps,
	BigDecimal dividend
) {
	public static StockSummaryDto from(final StockPriceView view) {
		return new StockSummaryDto(
			view.getName(),
			view.getTicker(),
			view.getMarketCap(),
			view.getPrice(),
			view.getChange(),
			view.getVolume(),
			view.getPer(),
			view.getEps(),
			view.getDividend()
		);
	}
}
