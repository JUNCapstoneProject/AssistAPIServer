package com.help.stockassistplatform.domain.stock.indexed.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.help.stockassistplatform.domain.stock.indexed.entity.CompanyIndexed;

public interface CompanyIndexedRepository extends JpaRepository<CompanyIndexed, Integer> {
	// Boolean Mode (prefix 검색)
	@Query(value = """
		SELECT *
		FROM company
		WHERE MATCH(ticker, name_kr, name_en) AGAINST(:query IN BOOLEAN MODE)
		LIMIT 10
		""", nativeQuery = true)
	List<CompanyIndexed> prefixSearch(@Param("query") String query);

	// Natural Mode (유사도 순)
	@Query(value = """
		SELECT *
		FROM company
		WHERE MATCH(ticker, name_kr, name_en) AGAINST(:query)
		LIMIT 10
		""", nativeQuery = true)
	List<CompanyIndexed> relevanceSearch(@Param("query") String query);

	/* ------------------------------------------------------------------
	 * ① 1‒2글자 / 접두어 검색  ―  LIKE  +  UNION
	 *    - ticker  : 대문자 접두어
	 *    - name_kr : 한글 접두어
	 *    - name_en : 영어 접두어
	 *    ※ 인덱스 없어도 소규모 테이블은 충분히 빠름
	 * ------------------------------------------------------------------ */
	@Query(value = """
		( SELECT c.*, 1 AS priority_order
		    FROM company c
		   WHERE UPPER(c.ticker) LIKE CONCAT(:qUpper, '%')
		   LIMIT 10 )
		UNION ALL
		( SELECT c.*, 2 AS priority_order
		    FROM company c
		   WHERE c.name_kr LIKE CONCAT(:q, '%')
		      OR c.name_en LIKE CONCAT(:q, '%')
		   LIMIT 10 )
		ORDER BY priority_order, ticker
		LIMIT 10
		""", nativeQuery = true)
	List<CompanyIndexed> prefixSearch(@Param("q") String q,
		@Param("qUpper") String qUpper);

	/* ------------------------------------------------------------------
	 * ② 3글자 이상  ―  기존 FULLTEXT 인덱스 활용
	 *    - Boolean 모드로 ‘*’ 접미어 허용 (prefix match)
	 *    - ticker 완전 일치 시 가중치 ↑
	 * ------------------------------------------------------------------ */
	@Query(value = """
		  SELECT c.*,
		         (CASE WHEN UPPER(c.ticker) = :qUpper THEN 1 ELSE 0 END) AS exact_hit,
		         MATCH(c.ticker, c.name_kr, c.name_en)
		           AGAINST(:booleanQuery IN BOOLEAN MODE) AS score
		    FROM company c
		   WHERE MATCH(c.ticker, c.name_kr, c.name_en)
		         AGAINST(:booleanQuery IN BOOLEAN MODE)
		ORDER BY exact_hit DESC, score DESC
		   LIMIT 10
		  """, nativeQuery = true)
	List<CompanyIndexed> fulltextSearch(@Param("booleanQuery") String booleanQuery,
		@Param("qUpper") String qUpper);
}
