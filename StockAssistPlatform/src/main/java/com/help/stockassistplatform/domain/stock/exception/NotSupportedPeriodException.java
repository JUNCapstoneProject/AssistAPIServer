package com.help.stockassistplatform.domain.stock.exception;

public class NotSupportedPeriodException extends IllegalArgumentException {
	public NotSupportedPeriodException(final String period) {
		super("지원하지 않는 간격: " + period);
		System.out.println("지원하지 않는 간격!");
	}
}
