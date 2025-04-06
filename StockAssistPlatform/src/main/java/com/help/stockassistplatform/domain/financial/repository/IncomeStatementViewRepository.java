package com.help.stockassistplatform.domain.financial.repository;

import com.help.stockassistplatform.domain.financial.entity.IncomeStatementView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeStatementViewRepository extends JpaRepository<IncomeStatementView, String> {
    List<IncomeStatementView> findTop2ByCompanyOrderByPostedAtDesc(String company);
}
