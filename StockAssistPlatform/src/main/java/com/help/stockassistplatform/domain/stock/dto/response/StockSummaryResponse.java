package com.help.stockassistplatform.domain.stock.dto.response;

import java.util.List;

public record StockSummaryResponse(
	List<StockSummaryDto> data
) {
}
