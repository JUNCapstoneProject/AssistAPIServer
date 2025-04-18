package com.help.stockassistplatform.domain.portfolio;

import static com.help.stockassistplatform.domain.portfolio.StockBalanceResponse.*;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class KisBalanceService {
	private final WebClient koreaInvestWebClient;
	private final KisAuthService kisAuthService;

	private static final String TR_ID_BALANCE = "VTTS3012R"; // 잔고조회
	private static final String TR_ID_CASH = "VTTS3007R";    // 예수금조회

	@Value("${kis.appkey}")
	private String appKey;

	@Value("${kis.appsecret}")
	private String appSecret;

	/**
	 * 해외 주식 잔고 조회 (output1 = 종목 리스트, output2 = 요약 정보)
	 */
	public StockBalanceResponse getParsedBalance(String cano, String acntPrdtCd, String excgCd, String currency) {
		final JsonNode raw = getOverseaBalance(cano, acntPrdtCd, excgCd, currency);
		final JsonNode output1 = raw.get("output1");
		final JsonNode output2 = raw.get("output2");
		final BigDecimal cash = getCashFromPsAmount(cano, acntPrdtCd);
		return from(output1, output2, cash);
	}

	/**
	 * 매수 가능 금액(USD) 조회
	 */
	public BigDecimal getCashFromPsAmount(final String cano, final String acntPrdtCd) {
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
			.headers(headers -> applyDefaultHeaders(headers, token, TR_ID_CASH))
			.retrieve()
			.bodyToMono(JsonNode.class)
			.block();

		if (!"0".equals(Objects.requireNonNull(response).get("rt_cd").asText())) {
			throw new RuntimeException("예수금 조회 실패: " + response.get("msg1").asText());
		}

		JsonNode output = response.get("output");
		System.out.println("output = " + output);
		return getDecimalSafe(output, "frcr_ord_psbl_amt1", "0");
	}

	/**
	 * WebClient로 KIS API 잔고 조회
	 */
	private JsonNode getOverseaBalance(String cano, String acntPrdtCd, String excgCd, String currency) {
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
			.headers(headers -> applyDefaultHeaders(headers, token, TR_ID_BALANCE))
			.retrieve()
			.bodyToMono(JsonNode.class)
			.block();
	}

	/**
	 * 공통 헤더 적용 메서드
	 */
	private void applyDefaultHeaders(HttpHeaders headers, String token, String trId) {
		headers.set("content-type", "application/json; charset=utf-8");
		headers.set("authorization", "Bearer " + token);
		headers.set("appkey", appKey);
		headers.set("appsecret", appSecret);
		headers.set("tr_id", trId);
		headers.set("custtype", "P");
	}

	/**
	 * 액세스 토큰 가져오기
	 */
	private String getAccessToken() {
		final String token = kisAuthService.getAccessToken();
		if (null == token || token.isEmpty()) {
			throw new RuntimeException("KIS Access Token 획득 실패");
		}
		log.debug("KIS Access Token: {}", token);
		return token;
	}
}
