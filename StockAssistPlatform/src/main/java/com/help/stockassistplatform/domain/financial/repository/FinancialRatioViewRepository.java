package com.help.stockassistplatform.domain.financial.repository;

import com.help.stockassistplatform.domain.financial.entity.FinancialRatioId;
import com.help.stockassistplatform.domain.financial.entity.FinancialRatioView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialRatioViewRepository extends JpaRepository<FinancialRatioView, FinancialRatioId> {
    List<FinancialRatioView> findTop2ByCompanyOrderByPostedAtDesc(String company);
}
