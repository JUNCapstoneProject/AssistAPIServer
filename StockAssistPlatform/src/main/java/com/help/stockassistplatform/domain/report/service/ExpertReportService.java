package com.help.stockassistplatform.domain.report.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.report.dto.response.ReportResponse;
import com.help.stockassistplatform.domain.report.expert.entity.ExpertReport;
import com.help.stockassistplatform.domain.report.expert.repository.ExpertReportRepository;

import jakarta.annotation.Nullable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpertReportService {

	private final ExpertReportRepository expertReportRepository;

	public Slice<ReportResponse> getExpertReports(final Pageable pageable, @Nullable final String category) {
		log.info("[GET]/api/reports, type=expert, category={}", category);

		final Slice<ExpertReport> slice = (null == category || category.isBlank())
			? expertReportRepository.findAllBy(pageable)
			: expertReportRepository.findAllByTagContaining(category, pageable);

		return slice.map(ReportResponse::from);
	}
}

