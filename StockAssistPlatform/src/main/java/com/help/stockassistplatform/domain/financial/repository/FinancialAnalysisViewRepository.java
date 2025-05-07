package com.help.stockassistplatform.domain.financial.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.help.stockassistplatform.domain.financial.entity.FinancialAnalysisId;
import com.help.stockassistplatform.domain.financial.entity.FinancialAnalysisView;

@Repository
public interface FinancialAnalysisViewRepository extends JpaRepository<FinancialAnalysisView, FinancialAnalysisId> {
	Optional<FinancialAnalysisView> findLatestByCompanyOrderByPostedAtDesc(String ticker);

	@Query("""
		    SELECT v FROM FinancialAnalysisView v
		    WHERE v.company IN :tickers AND v.postedAt = (
		        SELECT MAX(v2.postedAt)
		        FROM FinancialAnalysisView v2
		        WHERE v2.company = v.company
		    )
		""")
	List<FinancialAnalysisView> findLatestByTickers(@Param("tickers") List<String> tickers);

	@Query(
		value = "SELECT ai_analysis FROM financials_analysis_vw WHERE company = :company ORDER BY posted_at DESC LIMIT 1",
		nativeQuery = true
	)
	Optional<String> findLatestAiAnalysisByCompany(@Param("company") String company);
}
