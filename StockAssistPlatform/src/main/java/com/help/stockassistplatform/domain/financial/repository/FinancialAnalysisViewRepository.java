package com.help.stockassistplatform.domain.financial.repository;

import com.help.stockassistplatform.domain.financial.entity.FinancialAnalysisView;
import com.help.stockassistplatform.domain.financial.entity.FinancialAnalysisId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

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
}
