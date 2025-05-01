package com.help.stockassistplatform.domain.portfolio.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KisStockSummary(
	@JsonProperty("frcr_buy_amt_smtl1") BigDecimal investmentAmount,
	@JsonProperty("tot_evlu_pfls_amt") BigDecimal evaluationProfit,
	@JsonProperty("tot_pftrt") BigDecimal profitRate
) {
}
