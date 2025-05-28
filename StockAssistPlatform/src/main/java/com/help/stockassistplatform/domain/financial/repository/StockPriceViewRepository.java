package com.help.stockassistplatform.domain.financial.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.help.stockassistplatform.domain.financial.entity.StockPriceView;

@Repository
public interface StockPriceViewRepository extends JpaRepository<StockPriceView, String> {

	Optional<StockPriceView> findTop1ByTickerOrderByPostedAtDesc(String ticker);

	// ✅ Keyset 기반 슬라이싱 쿼리
	@Query(value = """
		    SELECT sp.*
		    FROM stock_vw sp
		    INNER JOIN (
		        SELECT ticker, MAX(posted_at) AS latest_posted_at
		        FROM stock_vw
		        GROUP BY ticker
		    ) latest ON sp.ticker = latest.ticker AND sp.posted_at = latest.latest_posted_at
		    WHERE (:lastTicker IS NULL OR sp.ticker > :lastTicker)
		    ORDER BY sp.ticker
		    LIMIT :limit
		""", nativeQuery = true)
	List<StockPriceView> findNextGroupedByTicker(@Param("lastTicker") String lastTicker, @Param("limit") int limit);

	@Query(value = """
			SELECT s.ticker, s.name_kr
			FROM stock_vw s
			JOIN (
				SELECT ticker, MAX(posted_at) AS latest_posted
				FROM stock_vw
				GROUP BY ticker
			) latest ON s.ticker = latest.ticker AND s.posted_at = latest.latest_posted
			WHERE CHAR_LENGTH(s.name_kr) <= 15
			ORDER BY s.market_cap DESC
			LIMIT 4
		""", nativeQuery = true)
	List<Object[]> findTop4TickerAndNameByMarketCap();

	@Query(value = """
		    SELECT *
		    FROM stock_vw sp
		    WHERE (sp.ticker, sp.posted_at) IN (
		        SELECT ticker, MAX(posted_at)
		        FROM stock_vw
		        GROUP BY ticker
		    )
		    ORDER BY market_cap DESC
		    LIMIT :limit OFFSET :offset
		""", nativeQuery = true)
	List<StockPriceView> findByMarketCapPaged(
		@Param("limit") int limit,
		@Param("offset") int offset
	);

	@Query(value = """
		    SELECT *
		    FROM stock_vw
		    WHERE ticker = :ticker
		      AND posted_at BETWEEN :start AND :end
		    ORDER BY posted_at ASC
		""", nativeQuery = true)
	List<StockPriceView> findTimeSeriesByTickerAndDateRange(
		@Param("ticker") String ticker,
		@Param("start") LocalDate start,
		@Param("end") LocalDate end
	);

	@Query(value = """
		    SELECT *
		    FROM (
		        SELECT *,
		               ROW_NUMBER() OVER (PARTITION BY ticker ORDER BY posted_at DESC, market_cap DESC) as rn
		        FROM stock_vw
		        WHERE ticker IN (:tickers)
		    ) ranked
		    WHERE rn = 1
		""", nativeQuery = true)
	List<StockPriceView> findLatestOnePerTicker(@Param("tickers") List<String> tickers);
}
