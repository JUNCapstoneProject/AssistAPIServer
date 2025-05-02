package com.help.stockassistplatform.domain.report.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.report.dto.request.UserReportRequest;
import com.help.stockassistplatform.domain.report.dto.response.ReportResponse;
import com.help.stockassistplatform.domain.report.dto.response.UserReportDetailResponse;
import com.help.stockassistplatform.domain.report.exception.ReportNotFoundException;
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

		if (slice.isEmpty()) {
			throw new ReportNotFoundException("리포트가 존재하지 않습니다.");
		}

		return slice.map(ReportResponse::from);
	}

	@Transactional
	public void createReport(final UserReportRequest request, final CustomUser userDetail) {
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
		final UserReport report = getReportOrThrow(reportId);
		final boolean isAuthor = isAuthor(report, userDetail);

		return UserReportDetailResponse.of(report, isAuthor);
	}

	@Transactional
	public void updateReport(
		final Long reportId,
		final UserReportRequest request,
		final CustomUser userDetail
	) {
		final UserReport report = getReportOrThrow(reportId);

		validateAuthor(report, userDetail);

		log.debug("Updating to {}", request.toString());
		report.update(
			request.title(),
			request.content(),
			request.category()
		);
	}

	@Transactional
	public void deleteReport(final Long reportId, final CustomUser userDetail) {
		final UserReport report = getReportOrThrow(reportId);

		validateAuthor(report, userDetail);

		log.debug("Deleting report {}", reportId);
		userReportRepository.delete(report);
	}

	private UserReport getReportOrThrow(final Long id) {
		return userReportRepository.findById(id)
			.orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
	}

	private void validateAuthor(final UserReport report, final CustomUser userDetail) {
		if (!isAuthor(report, userDetail)) {
			throw new CustomException(ErrorCode.NOT_FOUND); // 보안상 NOT_FOUND로 처리
		}
	}

	private boolean isAuthor(final UserReport report, final CustomUser userDetail) {
		final Long currentUserId = (null != userDetail) ? userDetail.getUserId() : null;
		return report.getUser().getUserId().equals(currentUserId);
	}
}
