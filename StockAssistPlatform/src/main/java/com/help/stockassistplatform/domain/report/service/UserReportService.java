package com.help.stockassistplatform.domain.report.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.report.dto.request.CreateUserReportRequest;
import com.help.stockassistplatform.domain.report.dto.response.ReportResponse;
import com.help.stockassistplatform.domain.report.dto.response.UserReportDetailResponse;
import com.help.stockassistplatform.domain.report.user.entity.UserReport;
import com.help.stockassistplatform.domain.report.user.repository.UserReportRepository;
import com.help.stockassistplatform.domain.user.entity.User;
import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;
import com.help.stockassistplatform.global.jwt.CustomUser;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReportService {
	private final UserReportRepository userReportRepository;

	public Slice<ReportResponse> getUserReports(final Pageable pageable, @Nullable final String category) {
		log.info("[GET]/api/reports, type=user, category={}", category);
		final Slice<UserReport> slice = (null == category || category.isBlank())
			? userReportRepository.findAllBy(pageable)
			: userReportRepository.findAllByCategoryContaining(category, pageable);

		return slice.map(ReportResponse::from);
	}

	@Transactional
	public void createReport(final CreateUserReportRequest request, final CustomUser userDetail) {
		final User userRef = User.ofId(userDetail.getUserId());

		final UserReport report = UserReport.builder()
			.title(request.title())
			.content(request.content())
			.category(request.category())
			.writerNickname(userDetail.getNickname())
			.user(userRef)
			.build();

		userReportRepository.save(report);
	}

	public UserReportDetailResponse getReportDetail(final Long reportId, final CustomUser userDetail) {
		final UserReport report = userReportRepository.findById(reportId)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));

		final Long currentUserId = (null != userDetail) ? userDetail.getUserId() : null;
		final boolean isAuthor = report.getUser().getUserId().equals(currentUserId);
		return UserReportDetailResponse.of(report, isAuthor);
	}
}
