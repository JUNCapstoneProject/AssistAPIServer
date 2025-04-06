package com.help.stockassistplatform.domain.financial.repository;

import com.help.stockassistplatform.domain.financial.entity.BalanceSheetView;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BalanceSheetViewRepository extends JpaRepository<BalanceSheetView, String> {
    List<BalanceSheetView> findTop2ByCompanyOrderByPostedAtDesc(String company);
}
