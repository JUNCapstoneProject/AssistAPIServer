package com.help.stockassistplatform.domain.financial.repository;

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

	@Query(value = "SELECT ticker, name_kr FROM stock_vw ORDER BY market_cap DESC LIMIT 8", nativeQuery = true)
	List<Object[]> findTop8TickerAndNameByMarketCap();
}
