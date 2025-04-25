package com.help.stockassistplatform.domain.portfolio.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.help.stockassistplatform.domain.portfolio.dto.StockBalanceResponse;

@Component
public class KisBalanceResponseMapper {
	public StockBalanceResponse map(final JsonNode rawBalance, final BigDecimal cash) {
		final JsonNode output1 = rawBalance.get("output1");
		final JsonNode output2 = rawBalance.get("output2");

		return StockBalanceResponse.from(output1, output2, cash);
	}
}
