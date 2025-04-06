package com.help.stockassistplatform.domain.financial.repository;

import com.help.stockassistplatform.domain.financial.entity.CashFlowView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CashFlowViewRepository extends JpaRepository<CashFlowView, String> {
    List<CashFlowView> findTop2ByCompanyOrderByPostedAtDesc(String company);
}
