package com.help.stockassistplatform.domain.portfolio.service;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.help.stockassistplatform.domain.portfolio.dto.response.StockBalanceResponse;
import com.help.stockassistplatform.global.config.properties.KisProperties;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KisBalanceService {

	private final KisApiClient kisApiClient;
	private final KisBalanceResponseMapper mapper;
	private final KisProperties kisProps;

	public StockBalanceResponse getParsedBalance(final String excgCd, final String currency) {
		final String cano = kisProps.cano();
		final String acntPrdtCd = kisProps.acntPrdtCd();

		final JsonNode balanceRaw = kisApiClient.fetchBalance(cano, acntPrdtCd, excgCd, currency);
		final BigDecimal cash = kisApiClient.fetchCash(cano, acntPrdtCd);
		return mapper.map(balanceRaw, cash);
	}
}
