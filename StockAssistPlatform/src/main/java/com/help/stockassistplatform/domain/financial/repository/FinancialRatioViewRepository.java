package com.help.stockassistplatform.domain.financial.repository;

import com.help.stockassistplatform.domain.financial.entity.FinancialRatioView;
import com.help.stockassistplatform.domain.financial.entity.FinancialRatioId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FinancialRatioViewRepository extends JpaRepository<FinancialRatioView, FinancialRatioId> {
    List<FinancialRatioView> findRecent2ByCompanyOrderByPostedAtDesc(String ticker);

    @Query("""
        SELECT v FROM FinancialRatioView v
        WHERE v.company IN :tickers
        ORDER BY v.company, v.postedAt DESC
    """)
    List<FinancialRatioView> findRecent2ByTickers(@Param("tickers") List<String> tickers);
}