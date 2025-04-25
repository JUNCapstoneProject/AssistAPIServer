package com.help.stockassistplatform.domain.portfolio.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KisCashOutput(
	@JsonProperty("frcr_ord_psbl_amt1") BigDecimal orderableAmount,
	@JsonProperty("exrt") BigDecimal exchangeRate
) {
}
