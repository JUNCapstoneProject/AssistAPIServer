package com.help.stockassistplatform.domain.financial.repository;

import com.help.stockassistplatform.domain.financial.entity.FinancialAnalysisId;
import com.help.stockassistplatform.domain.financial.entity.FinancialAnalysisView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FinancialAnalysisViewRepository extends JpaRepository<FinancialAnalysisView, FinancialAnalysisId> {
    Optional<FinancialAnalysisView> findTop1ByCompanyOrderByPostedAtDesc(String company);
}
