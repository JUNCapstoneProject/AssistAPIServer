package com.help.stockassistplatform.domain.stock.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.help.stockassistplatform.domain.stock.entity.StockView;

public interface StockViewRepository extends JpaRepository<StockView, String> {
	@Query(value = """
		    SELECT company_id, ticker, name_kr, name_en
			FROM company_vw
			WHERE ticker LIKE CONCAT('%', :query, '%')
		    OR name_kr LIKE CONCAT('%', :query, '%')
		    OR name_en LIKE CONCAT('%', :query, '%')
			ORDER BY company_id
			LIMIT 10;
		""", nativeQuery = true)
	List<StockView> searchPreview(@Param("query") String query);
}
