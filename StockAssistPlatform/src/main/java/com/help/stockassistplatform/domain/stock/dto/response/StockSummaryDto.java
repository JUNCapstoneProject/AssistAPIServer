package com.help.stockassistplatform.domain.stock.dto.response;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.help.stockassistplatform.domain.financial.entity.StockPriceView;

public class StockSummaryDto {
	private final String name;
	private final String symbol;
	private final BigInteger marketCap;
	private final BigDecimal price;
	private final Float change;
	private final Long volume;
	private final BigDecimal pe;
	private final BigDecimal eps;
	private final BigDecimal dividend;

	private StockSummaryDto(final StockPriceView view) {
		this.name = view.getName();
		this.symbol = view.getTicker();
		this.marketCap = view.getMarketCap();
		this.price = view.getClose();
		this.change = view.getChange();
		this.volume = view.getVolume();
		this.pe = view.getPer();
		this.eps = view.getEps();
		this.dividend = view.getDividend();
	}

	public static StockSummaryDto from(final StockPriceView view) {
		return new StockSummaryDto(view);
	}
}
