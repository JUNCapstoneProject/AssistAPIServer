package com.help.stockassistplatform.domain.stock.batch;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.help.stockassistplatform.domain.stock.service.CompanyIndexedSyncService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class CompanySyncScheduler {

	private final CompanyIndexedSyncService syncService;

	@Scheduled(cron = "0 0 1 * * *") // 매일 1시
	public void syncCompanyData() {
		log.info("Start syncing company data");
		syncService.sync();
	}
}
