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

	Optional<StockPriceView> findOneByTicker(String ticker);

	@Query(value = """
		SELECT sp.ticker
		FROM stock_vw sp
		ORDER BY sp.market_cap DESC
		LIMIT :limit OFFSET :offset
	""", nativeQuery = true)
	List<String> findPagedTickers(@Param("limit") int limit, @Param("offset") int offset);

	@Query(value = """
			SELECT s.ticker, s.name_kr
			FROM stock_vw s
			WHERE CHAR_LENGTH(s.name_kr) <= 15
			ORDER BY s.market_cap DESC
			LIMIT 4
		""", nativeQuery = true)
	List<Object[]> findTop4TickerAndNameByMarketCap();

	@Query(value = """
		    SELECT *
		    FROM stock_vw sp
		    ORDER BY market_cap DESC
		    LIMIT :limit OFFSET :offset
		""", nativeQuery = true)
	List<StockPriceView> findByMarketCapPaged(
		@Param("limit") int limit,
		@Param("offset") int offset
	);

	List<StockPriceView> findByTickerIn(List<String> tickers);
}
