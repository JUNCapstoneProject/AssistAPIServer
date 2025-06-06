package com.help.stockassistplatform.domain.news.entity;

import java.util.Arrays;
import java.util.Optional;

public enum Lang {
	KO, EN;

	public static Lang of(final String code) {
		return Optional.ofNullable(code)
			.map(String::trim)
			.map(String::toUpperCase)
			.flatMap(c -> Arrays.stream(values())
				.filter(v -> v.name().equals(c))
				.findFirst())
			.orElse(KO);
	}
}
