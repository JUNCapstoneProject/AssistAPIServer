package com.help.stockassistplatform.domain.stock.service.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.financial.entity.StockPriceView;
import com.help.stockassistplatform.domain.financial.repository.StockPriceViewRepository;
import com.help.stockassistplatform.domain.stock.dto.response.StockPriceTimeSeriesResponse;
import com.help.stockassistplatform.domain.stock.dto.response.StockSummaryDto;
import com.help.stockassistplatform.domain.stock.dto.response.StockSummaryResponse;
import com.help.stockassistplatform.domain.stock.dto.response.TimeSeriesData;
import com.help.stockassistplatform.domain.stock.service.StockQueryService;
import com.help.stockassistplatform.global.common.IntervalResolver;
import com.help.stockassistplatform.global.common.TimeSeriesGrouper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StockQueryServiceImpl implements StockQueryService {

	private final StockPriceViewRepository stockPriceViewRepository;

	@Override
	public StockSummaryResponse getStockSummaries(final List<String> symbols) {
		final List<StockPriceView> views = stockPriceViewRepository.findLatestOnePerTicker(symbols);
		final List<StockSummaryDto> summaries = views.stream()
			.map(StockSummaryDto::from)
			.toList();

		return new StockSummaryResponse(summaries);
	}

	@Override
	public StockPriceTimeSeriesResponse getPriceTimeSeries(
		String symbol,
		LocalDate start,
		LocalDate end,
		String period
	) {
		final String interval = IntervalResolver.resolve(period);
		final List<StockPriceView> raw = stockPriceViewRepository.findAllInPeriod(symbol, start, end);
		final List<Map<String, Object>> timeSeries = TimeSeriesGrouper.group(raw, start, end, interval).stream()
			.map(d -> {
				Map<String, Object> map = new LinkedHashMap<>();
				map.put("date", d.date());
				map.put(symbol, d.price());
				return map;
			})
			.toList();
		return new StockPriceTimeSeriesResponse(timeSeries, interval);
	}

	private List<LocalDate> createPeriodBoundaries(LocalDate start, LocalDate end, String interval) {
		List<LocalDate> boundaries = new ArrayList<>();
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

	private List<TimeSeriesData> groupByPeriodFromStart(
		List<StockPriceView> raw,
		LocalDate start,
		LocalDate end,
		String interval
	) {
		final List<LocalDate> boundaries = createPeriodBoundaries(start, end, interval);

		return boundaries.stream()
			.map(periodStart -> {
				LocalDate periodEnd = switch (interval) {
					case "daily" -> periodStart;
					case "weekly" -> periodStart.plusDays(6);
					case "monthly" -> periodStart.withDayOfMonth(periodStart.lengthOfMonth());
					default -> throw new IllegalArgumentException("지원하지 않는 interval: " + interval);
				};
				log.debug(periodStart.toString());
				log.debug(periodEnd.toString());

				return raw.stream()
					.filter(r -> {
						LocalDate date = r.getPostedAt().toLocalDate();
						return !date.isBefore(periodStart) && !date.isAfter(periodEnd);
					})
					.min(Comparator.comparing(StockPriceView::getPostedAt))  // 최신
					.map(view -> new TimeSeriesData(view.getPostedAt().toLocalDate(), view.getClose()))
					.orElse(null);
			})
			.filter(Objects::nonNull)
			.toList();
	}
}
