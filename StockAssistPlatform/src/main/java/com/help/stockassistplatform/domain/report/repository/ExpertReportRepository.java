package com.help.stockassistplatform.domain.report.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.help.stockassistplatform.domain.report.entity.ExpertReport;

@Repository
public interface ExpertReportRepository extends JpaRepository<ExpertReport, Long> {
	Slice<ExpertReport> findAllBy(Pageable pageable);

	List<ExpertReport> findByTagContaining(String keyword);
}
