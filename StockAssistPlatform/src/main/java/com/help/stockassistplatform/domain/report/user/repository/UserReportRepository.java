package com.help.stockassistplatform.domain.report.user.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.help.stockassistplatform.domain.report.user.entity.UserReport;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {
	Slice<UserReport> findAllBy(Pageable pageable);

	Slice<UserReport> findAllByCategoryContaining(String category, Pageable pageable);
}
