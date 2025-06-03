package com.help.stockassistplatform.domain.financial.repository;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.help.stockassistplatform.domain.financial.entity.StockChartView;

@Repository
public interface StockChartViewRepository extends JpaRepository<StockChartView, String> {

	/**
	 * 시작과 끝 날짜 사이의 종목 차트 데이터를 전체 조회합니다.
	 *
	 * @param ticker 종목 코드
	 * @param start 시작 날짜
	 * @param end 종료 날짜
	 * @return 종목 차트 데이터
	 */
	List<StockChartView> findAllByTickerAndPostedAtBetween(String ticker, LocalDateTime start, LocalDateTime end);

}
