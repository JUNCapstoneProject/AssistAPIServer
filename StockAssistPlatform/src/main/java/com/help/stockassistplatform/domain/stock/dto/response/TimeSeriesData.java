package com.help.stockassistplatform.domain.stock.dto.response;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TimeSeriesData(
	LocalDate date,
	BigDecimal price
) {
}
