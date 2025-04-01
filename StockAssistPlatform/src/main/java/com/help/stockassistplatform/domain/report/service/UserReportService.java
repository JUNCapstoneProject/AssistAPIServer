package com.help.stockassistplatform.domain.report.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.report.dto.ReportResponse;
import com.help.stockassistplatform.domain.report.user.entity.UserReport;
import com.help.stockassistplatform.domain.report.user.repository.UserReportRepository;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReportService {
	private final UserReportRepository userReportRepository;

	public List<ReportResponse> getUserReports(final Pageable pageable, @Nullable final String category) {
		log.info("[GET]/api/reports, type=user, category={}", category);
		final Slice<UserReport> slice = (null == category || category.isBlank())
			? userReportRepository.findAllBy(pageable)
			: userReportRepository.findAllByCategoryContaining(category, pageable);

		return slice.stream()
			.map(ReportResponse::from)
			.toList();
	}
}
