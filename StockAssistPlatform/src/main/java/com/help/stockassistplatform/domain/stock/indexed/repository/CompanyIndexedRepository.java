package com.help.stockassistplatform.domain.stock.indexed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.help.stockassistplatform.domain.stock.indexed.entity.CompanyIndexed;

public interface CompanyIndexedRepository extends JpaRepository<CompanyIndexed, Integer> {
	// 검색용 Native Query
	@Query(value = """
		SELECT * FROM company_indexed
		WHERE MATCH(name_kr, name_en) AGAINST(:query)
		LIMIT 10
		""", nativeQuery = true)
	List<CompanyIndexed> searchPreview(@Param("query") String query);

}
