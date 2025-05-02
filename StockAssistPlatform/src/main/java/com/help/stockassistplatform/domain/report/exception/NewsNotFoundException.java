package com.help.stockassistplatform.domain.report.exception;

public class NewsNotFoundException extends RuntimeException {
	public NewsNotFoundException(final String message) {
		super(message);
	}
}
