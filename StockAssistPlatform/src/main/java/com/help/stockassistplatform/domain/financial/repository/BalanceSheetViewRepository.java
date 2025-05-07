package com.help.stockassistplatform.domain.financial.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.help.stockassistplatform.domain.financial.entity.BalanceSheetView;

@Repository
public interface BalanceSheetViewRepository extends JpaRepository<BalanceSheetView, String> {
	List<BalanceSheetView> findRecent2ByCompanyOrderByPostedAtDesc(String ticker);

	@Query("""
		    SELECT v FROM BalanceSheetView v
		    WHERE v.company IN :tickers
		    ORDER BY v.company, v.postedAt DESC
		""")
	List<BalanceSheetView> findRecent2ByTickers(@Param("tickers") List<String> tickers);
}
