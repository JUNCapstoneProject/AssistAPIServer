package com.help.stockassistplatform.domain.report.user.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.help.stockassistplatform.domain.report.user.entity.UserReport;
import com.help.stockassistplatform.domain.user.entity.User;

import jakarta.transaction.Transactional;

@Repository
public interface UserReportRepository extends JpaRepository<UserReport, Long> {
	Slice<UserReport> findAllBy(Pageable pageable);

	Slice<UserReport> findAllByCategoryContaining(String category, Pageable pageable);

	@Transactional
	@Modifying(clearAutomatically = true)
	@Query("DELETE FROM UserReport ur WHERE ur.user = :user")
	void bulkDeleteByUser(@Param("user") User user);

	Optional<UserReport> findById(UUID id);
}
