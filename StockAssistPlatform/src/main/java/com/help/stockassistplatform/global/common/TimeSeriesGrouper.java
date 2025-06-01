package com.help.stockassistplatform.global.common;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.help.stockassistplatform.domain.financial.entity.StockChartView;
import com.help.stockassistplatform.domain.stock.dto.response.TimeSeriesData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeSeriesGrouper {

	private TimeSeriesGrouper() {
		throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
	}

	public static List<TimeSeriesData> group(
		final List<StockChartView> raw,
		final LocalDate start,
		final LocalDate end,
		final String interval
	) {
		Map<LocalDate, StockChartView> earliest = new LinkedHashMap<>();

		for (StockChartView v : raw) {
			LocalDate date = v.getPostedAt().toLocalDate();
			if (date.isBefore(start) || date.isAfter(end))
				continue;

			LocalDate bucket = switch (interval) {
				case "daily" -> date;
				case "weekly" -> date.with(DayOfWeek.MONDAY);
				case "monthly" -> date.withDayOfMonth(1);
				default -> throw new IllegalArgumentException("지원하지 않는 interval: " + interval);
			};

			earliest.compute(bucket,
				(k, old) -> old == null || v.getPostedAt().isBefore(old.getPostedAt()) ? v : old);
		}

		return earliest.entrySet().stream()
			.sorted(Map.Entry.comparingByKey())
			.map(e -> new TimeSeriesData(
				e.getValue().getPostedAt().toLocalDate(),
				e.getValue().getAdjClose()))
			.toList();
	}
}
