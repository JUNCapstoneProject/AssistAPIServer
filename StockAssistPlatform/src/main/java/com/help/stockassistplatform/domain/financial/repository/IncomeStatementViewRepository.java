package com.help.stockassistplatform.domain.financial.repository;

import com.help.stockassistplatform.domain.financial.entity.IncomeStatementView;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IncomeStatementViewRepository extends JpaRepository<IncomeStatementView, String> {
    List<IncomeStatementView> findRecent2ByCompanyOrderByPostedAtDesc(String ticker);

    @Query("""
        SELECT v FROM IncomeStatementView v
        WHERE v.company IN :tickers
        ORDER BY v.company, v.postedAt DESC
    """)
    List<IncomeStatementView> findRecent2ByTickers(@Param("tickers") List<String> tickers);
}