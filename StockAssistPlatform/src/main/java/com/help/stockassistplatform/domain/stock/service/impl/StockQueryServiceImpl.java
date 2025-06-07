package com.help.stockassistplatform.domain.stock.service.impl;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.financial.entity.StockChartView;
import com.help.stockassistplatform.domain.financial.entity.StockPriceView;
import com.help.stockassistplatform.domain.financial.repository.StockChartViewRepository;
import com.help.stockassistplatform.domain.financial.repository.StockPriceViewRepository;
import com.help.stockassistplatform.domain.stock.dto.response.StockPriceTimeSeriesResponse;
import com.help.stockassistplatform.domain.stock.dto.response.StockSummaryDto;
import com.help.stockassistplatform.domain.stock.dto.response.StockSummaryResponse;
import com.help.stockassistplatform.domain.stock.service.StockQueryService;
import com.help.stockassistplatform.domain.wishlist.dto.StockPriceInfoDto;
import com.help.stockassistplatform.global.common.IntervalResolver;
import com.help.stockassistplatform.global.common.TimeSeriesGrouper;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Primary
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class StockQueryServiceImpl implements StockQueryService {

	private final StockPriceViewRepository stockPriceViewRepository;
	private final StockChartViewRepository stockChartViewRepository;

	@Override
	public StockSummaryResponse getStockSummaries(final List<String> symbols) {
		final List<StockPriceView> views = stockPriceViewRepository.findByTickerIn(symbols);
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
		final List<StockChartView> raw = stockChartViewRepository
			.findAllByTickerAndPostedAtBetween(symbol, start.atStartOfDay(), end.atTime(LocalTime.MAX));

		log.debug("종목: {}, 기간: {} ~ {}, 조회된 데이터 수: {}", symbol, start, end, raw.size());

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

	public Optional<StockPriceInfoDto> getSummary(final String ticker) {
		return stockPriceViewRepository.findByTicker(ticker.toUpperCase(Locale.ROOT))
			.map(StockPriceInfoDto::from);
	}
}
