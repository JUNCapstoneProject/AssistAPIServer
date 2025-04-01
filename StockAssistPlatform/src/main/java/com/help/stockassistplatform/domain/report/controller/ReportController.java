package com.help.stockassistplatform.domain.report.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.domain.report.dto.ReportType;
import com.help.stockassistplatform.domain.report.service.ExpertReportService;
import com.help.stockassistplatform.domain.report.service.UserReportService;
import com.help.stockassistplatform.global.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/reports")
@RequiredArgsConstructor
public class ReportController {
	private final ExpertReportService expertReportService;
	private final UserReportService userReportService;

	@GetMapping
	public ApiResponse<?> getReports(
		@RequestParam(defaultValue = "expert") final String type,
		@RequestParam(defaultValue = "1") final int page,
		@RequestParam(defaultValue = "6") final int limit,
		@RequestParam(required = false) final String category
	) {
		final ReportType reportType = ReportType.from(type);
		final Pageable pageable = PageRequest.of(page - 1, limit,
			Sort.by(Sort.Direction.DESC, reportType.getSortKey()));

		return switch (reportType) {
			case EXPERT -> ApiResponse.success(expertReportService.getExpertReports(pageable, category));
			case USER -> ApiResponse.success(userReportService.getUserReports(pageable, category));
		};
	}
}
