package com.help.stockassistplatform.global.common;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;

import com.help.stockassistplatform.domain.financial.entity.StockPriceView;
import com.help.stockassistplatform.domain.stock.dto.response.StockPriceTimeSeriesResponse.TimeSeriesData;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TimeSeriesGrouper {

	private TimeSeriesGrouper() {
		throw new UnsupportedOperationException("유틸리티 클래스는 인스턴스화할 수 없습니다.");
	}

	public static List<TimeSeriesData> group(
		final List<StockPriceView> raw,
		final LocalDate start,
		final LocalDate end,
		final String interval
	) {
		final List<LocalDate> boundaries = createPeriodBoundaries(start, end, interval);

		return boundaries.stream()
			.map(periodStart -> {
				final LocalDate periodEnd = switch (interval) {
					case "daily" -> periodStart;
					case "weekly" -> periodStart.plusDays(6);
					case "monthly" -> periodStart.withDayOfMonth(periodStart.lengthOfMonth());
					default -> throw new IllegalArgumentException("지원하지 않는 interval: " + interval);
				};

				log.debug("Grouping: {} ~ {}", periodStart, periodEnd);

				return raw.stream()
					.filter(r -> {
						LocalDate date = r.getPostedAt().toLocalDate();
						return !date.isBefore(periodStart) && !date.isAfter(periodEnd);
					})
					.min(Comparator.comparing(StockPriceView::getPostedAt))
					.map(view -> new TimeSeriesData(view.getPostedAt().toLocalDate(), view.getClose()))
					.orElse(null);
			})
			.filter(Objects::nonNull)
			.toList();
	}

	private static List<LocalDate> createPeriodBoundaries(
		LocalDate start,
		LocalDate end,
		String interval
	) {
		final List<LocalDate> boundaries = new ArrayList<>();
		LocalDate cursor = start;

		while (!cursor.isAfter(end)) {
			boundaries.add(cursor);
			cursor = switch (interval) {
				case "daily" -> cursor.plusDays(1);
				case "weekly" -> cursor.plusWeeks(1);
				case "monthly" -> cursor.plusMonths(1);
				default -> throw new IllegalArgumentException("지원하지 않는 interval: " + interval);
			};
		}
		return boundaries;
	}
}
