package com.help.stockassistplatform.domain.portfolio.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.stockassistplatform.domain.portfolio.dto.response.KisStockItem;
import com.help.stockassistplatform.domain.portfolio.dto.response.KisStockSummary;
import com.help.stockassistplatform.domain.portfolio.dto.response.StockBalanceResponse;
import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KisBalanceResponseMapper {
	private final ObjectMapper objectMapper;

	public StockBalanceResponse map(final JsonNode rawBalance, final BigDecimal cash) {
		try {
			final List<KisStockItem> stockItems = objectMapper
				.readerForListOf(KisStockItem.class)
				.readValue(rawBalance.get("output1"));

			final KisStockSummary summary = objectMapper
				.treeToValue(rawBalance.get("output2"), KisStockSummary.class);

			return StockBalanceResponse.from(summary, stockItems, cash);

		} catch (Exception e) {
			throw new CustomException(ErrorCode.JSON_PARSING_FAILURE);
		}
	}
}
