package com.help.stockassistplatform.domain.report.service;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.report.dto.ReportResponse;
import com.help.stockassistplatform.domain.report.repository.ExpertReportRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ExpertReportService {

	private final ExpertReportRepository expertReportRepository;

	public List<ReportResponse> getExpertReports(final Pageable pageable) {
		log.info("[GET]/api/reports, type=expert");
		return expertReportRepository.findAll(pageable).stream()
			.map(ReportResponse::from)
			.toList();
	}
}

