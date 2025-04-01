package com.help.stockassistplatform.domain.report.dto;

import java.util.Arrays;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ReportType {
	EXPERT("date"),
	USER("createdAt");

	@Getter
	private final String sortKey;

	public static ReportType from(final String value) {
		return Arrays.stream(values())
			.filter(t -> t.name().equalsIgnoreCase(value))
			.findFirst()
			.orElse(EXPERT);
	}
}
