package com.help.stockassistplatform.domain.financial.repository;

import com.help.stockassistplatform.domain.financial.entity.StockPriceView;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockPriceViewRepository extends JpaRepository<StockPriceView, String> {

    // 단일 종목: 가장 최근 데이터
    Optional<StockPriceView> findTop1ByTickerOrderByPostedAtDesc(String ticker);

    // 전체 종목: ticker별 최신 1건 (페이지네이션)
    @Query(value = """
        SELECT sp.*
        FROM stock_vw sp
        INNER JOIN (
            SELECT ticker, MAX(posted_at) AS latest_posted_at
            FROM stock_vw
            GROUP BY ticker
        ) latest ON sp.ticker = latest.ticker AND sp.posted_at = latest.latest_posted_at
        ORDER BY sp.ticker
        LIMIT :#{#pageable.pageSize}
        OFFSET :#{#pageable.offset}
    """, nativeQuery = true)
    List<StockPriceView> findAllGroupedByTicker(Pageable pageable);

    // ticker 종류 수 (총 페이지 계산용)
    @Query("SELECT COUNT(DISTINCT s.ticker) FROM StockPriceView s")
    long countDistinctTicker();
}
