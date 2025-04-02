package com.help.stockassistplatform.domain.report.controller;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.domain.report.dto.request.CreateUserReportRequest;
import com.help.stockassistplatform.domain.report.dto.request.ReportType;
import com.help.stockassistplatform.domain.report.dto.response.ReportSliceResponse;
import com.help.stockassistplatform.domain.report.dto.response.UserReportDetailResponse;
import com.help.stockassistplatform.domain.report.service.ExpertReportService;
import com.help.stockassistplatform.domain.report.service.UserReportService;
import com.help.stockassistplatform.global.common.response.ApiResponse;
import com.help.stockassistplatform.global.jwt.CustomUser;

import jakarta.validation.Valid;
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
		final Pageable pageable = PageRequest.of(
			page - 1,
			limit,
			Sort.by(Sort.Direction.DESC, reportType.getSortKey())
		);

		return switch (reportType) {
			case EXPERT -> ApiResponse.success(
				ReportSliceResponse.from(expertReportService.getExpertReports(pageable, category))
			);
			case USER -> ApiResponse.success(
				ReportSliceResponse.from(userReportService.getUserReports(pageable, category))
			);
		};
	}

	@PreAuthorize("isAuthenticated()")
	@PostMapping
	public ApiResponse<?> createReport(
		@RequestBody @Valid final CreateUserReportRequest request,
		@AuthenticationPrincipal final CustomUser userDetail
	) {
		userReportService.createReport(request, userDetail);
		return ApiResponse.success(null);
	}

	@GetMapping("/{id}")
	public ApiResponse<?> getUserReport(
		@PathVariable final Long id,
		@AuthenticationPrincipal final CustomUser userDetail
	) {
		final UserReportDetailResponse response = userReportService.getReportDetail(id, userDetail);
		return ApiResponse.success(response);
	}
}
