package com.help.stockassistplatform.domain.report.dto;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.help.stockassistplatform.domain.report.entity.ExpertReport;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ReportResponse(
	Long id,
	String category,
	String source,
	String title,
	String description,
	LocalDateTime date,
	String link
) {

	public static ReportResponse from(final ExpertReport report) {
		return new ReportResponse(
			null,
			report.getTag(),
			report.getAuthor(),
			report.getTitle(),
			summarize(report.getContent()),
			report.getDate(),
			report.getLink()
		);
	}

	// public static ReportResponse from(UserReport report) {
	// 	return new ReportResponse(
	// 		report.getId(),
	// 		report.getCategory(),
	// 		report.getSource(),
	// 		report.getTitle(),
	// 		report.getDescription(),
	// 		report.getDate(),
	// 		null
	// 	);
	// }

	private static String summarize(final String content) {
		return null != content ? content.substring(0, Math.min(100, content.length())) + "..." : "";
	}
}

