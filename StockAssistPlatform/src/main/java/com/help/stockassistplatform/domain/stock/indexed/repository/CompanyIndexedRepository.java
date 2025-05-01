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
		WHERE MATCH(ticker, name_kr, name_en) AGAINST(:query IN BOOLEAN MODE)
		LIMIT 10
		""", nativeQuery = true)
	List<CompanyIndexed> searchPreview(@Param("query") String query);

	// Boolean Mode (prefix 검색)
	@Query(value = """
		SELECT *
		FROM company_indexed
		WHERE MATCH(ticker, name_kr, name_en) AGAINST(:query IN BOOLEAN MODE)
		LIMIT 10
		""", nativeQuery = true)
	List<CompanyIndexed> prefixSearch(@Param("query") String query);

	// Natural Mode (유사도 순)
	@Query(value = """
		SELECT *
		FROM company_indexed
		WHERE MATCH(ticker, name_kr, name_en) AGAINST(:query)
		LIMIT 10
		""", nativeQuery = true)
	List<CompanyIndexed> relevanceSearch(@Param("query") String query);
}
