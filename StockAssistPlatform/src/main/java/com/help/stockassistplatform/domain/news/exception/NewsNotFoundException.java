package com.help.stockassistplatform.domain.news.exception;

public class NewsNotFoundException extends RuntimeException {
	public NewsNotFoundException(final String message) {
		super(message);
	}
}
