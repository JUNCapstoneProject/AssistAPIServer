package com.help.stockassistplatform.domain.report.dto.response;

import com.help.stockassistplatform.domain.report.user.entity.UserReport;

public record UserReportDetailResponse(
	String content,
	boolean isAuthor
) {
	public static UserReportDetailResponse of(final UserReport report, final boolean isAuthor) {
		return new UserReportDetailResponse(report.getContent(), isAuthor);
	}
}
