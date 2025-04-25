package com.help.stockassistplatform.domain.portfolio.service;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;
import com.help.stockassistplatform.global.config.properties.KisProperties;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class KisApiClient {

	private static final String TR_ID_BALANCE = "VTTS3012R";
	private static final String TR_ID_CASH = "VTTS3007R";

	private final WebClient koreaInvestWebClient;
	private final KisAuthService kisAuthService;

	private final KisProperties kisProps;

	public JsonNode fetchBalance(
		final String cano,
		final String acntPrdtCd,
		final String excgCd,
		final String currency
	) {
		final String token = getAccessToken();

		return koreaInvestWebClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/uapi/overseas-stock/v1/trading/inquire-balance")
				.queryParam("CANO", cano)
				.queryParam("ACNT_PRDT_CD", acntPrdtCd)
				.queryParam("OVRS_EXCG_CD", excgCd)
				.queryParam("TR_CRCY_CD", currency)
				.queryParam("CTX_AREA_FK200", "")
				.queryParam("CTX_AREA_NK200", "")
				.build())
			.headers(h -> applyHeaders(h, token, TR_ID_BALANCE))
			.retrieve()
			.bodyToMono(JsonNode.class)
			.block();
	}

	public BigDecimal fetchCash(final String cano, final String acntPrdtCd) {
		final String token = getAccessToken();

		final JsonNode response = koreaInvestWebClient.get()
			.uri(uriBuilder -> uriBuilder
				.path("/uapi/overseas-stock/v1/trading/inquire-psamount")
				.queryParam("CANO", cano)
				.queryParam("ACNT_PRDT_CD", acntPrdtCd)
				.queryParam("OVRS_EXCG_CD", "NASD")
				.queryParam("OVRS_ORD_UNPR", "240.0")
				.queryParam("ITEM_CD", "TSLA")
				.build())
			.headers(h -> applyHeaders(h, token, TR_ID_CASH))
			.retrieve()
			.bodyToMono(JsonNode.class)
			.block();

		if (!"0".equals(Objects.requireNonNull(response).path("rt_cd").asText()))
			throw new CustomException(ErrorCode.KIS_DEPOSIT_QUERY_FAILURE);

		final JsonNode output = response.get("output");
		final BigDecimal amount = getDecimalSafe(output, "frcr_ord_psbl_amt1", "0");
		final BigDecimal rate = getDecimalSafe(output, "exrt", "1000.0");
		return amount.multiply(rate);
	}

	private void applyHeaders(final HttpHeaders headers, final String token, final String trId) {
		headers.set("content-type", "application/json; charset=utf-8");
		headers.set("authorization", "Bearer " + token);
		headers.set("appkey", kisProps.appkey());
		headers.set("appsecret", kisProps.appsecret());
		headers.set("tr_id", trId);
		headers.set("custtype", "P");
	}

	private String getAccessToken() {
		final String token = kisAuthService.getAccessToken();
		if (null == token || token.isBlank())
			throw new CustomException(ErrorCode.KIS_ACCESS_TOKEN_FAILURE);
		return token;
	}

	private BigDecimal getDecimalSafe(final JsonNode node, final String field, final String fallback) {
		return new BigDecimal(node.path(field).asText(fallback));
	}
}
