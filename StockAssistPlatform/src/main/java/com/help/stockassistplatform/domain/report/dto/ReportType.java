package com.help.stockassistplatform.domain.report.dto;

import java.util.Arrays;

public enum ReportType {
	EXPERT, USER;

	public static ReportType from(final String value) {
		return Arrays.stream(values())
			.filter(t -> t.name().equalsIgnoreCase(value))
			.findFirst()
			.orElse(EXPERT);
	}
}
