package com.help.stockassistplatform.domain.report.dto;

import java.time.LocalDateTime;
import java.util.Optional;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.help.stockassistplatform.domain.report.expert.entity.ExpertReport;
import com.help.stockassistplatform.domain.report.user.entity.UserReport;

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
			Optional.ofNullable(report.getTag()).orElse("기타"),
			report.getAuthor(),
			report.getTitle(),
			summarize(report.getContent()),
			report.getDate(),
			report.getLink()
		);
	}

	public static ReportResponse from(final UserReport report) {
		return new ReportResponse(
			report.getId(),
			report.getCategory(),
			report.getWriterNickname(),
			report.getTitle(),
			report.getDescription(),
			report.getCreatedAt(),
			null
		);
	}

	private static String summarize(final String content) {
		return null != content ? content.substring(0, Math.min(100, content.length())) + "..." : "";
	}
}

