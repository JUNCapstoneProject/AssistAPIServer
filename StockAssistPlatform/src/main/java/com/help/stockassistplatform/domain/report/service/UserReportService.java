package com.help.stockassistplatform.domain.report.service;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserReportService {
	public Object getUserReports(final Pageable pageable) {
		log.info("[GET]/api/reports, type=user");
		return null;
	}
}
