package com.help.stockassistplatform.domain.stock.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.stock.indexed.entity.CompanyIndexed;
import com.help.stockassistplatform.domain.stock.indexed.repository.CompanyIndexedRepository;
import com.help.stockassistplatform.domain.stock.view.entity.CompanyView;
import com.help.stockassistplatform.domain.stock.view.repository.CompanyViewRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyIndexedSyncService {
	private final CompanyViewRepository companyViewRepository; // 외부 DB
	private final CompanyIndexedRepository companyIndexedRepository; // 내부 DB

	@Transactional
	public void sync() {
		final List<CompanyView> viewList = companyViewRepository.findAll();

		companyIndexedRepository.deleteAllInBatch();
		final List<CompanyIndexed> indexed = viewList.stream()
			.map(CompanyIndexed::from)
			.toList();

		companyIndexedRepository.saveAll(indexed);
	}
}
