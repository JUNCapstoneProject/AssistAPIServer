package com.help.stockassistplatform.global.common;

import com.help.stockassistplatform.domain.stock.exception.NotSupportedPeriodException;

public class IntervalResolver {

	public static String resolve(final String period) {
		return switch (period.toUpperCase()) {
			case "1M" -> "daily";
			case "6M", "YTD", "1Y" -> "weekly";
			case "5Y", "10Y", "MAX", "ALL" -> "monthly";
			default -> throw new NotSupportedPeriodException(period);
		};
	}
}
