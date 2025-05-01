package com.help.stockassistplatform.domain.portfolio.dto.response;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonAlias;

public record KisStockItem(
	@JsonAlias("ovrs_pdno") String symbol,
	@JsonAlias("ovrs_item_name") String name,
	@JsonAlias("now_pric2") BigDecimal currentPrice,
	@JsonAlias("frcr_evlu_pfls_amt") BigDecimal evalProfit,
	@JsonAlias("ovrs_cblc_qty") BigDecimal quantity,
	@JsonAlias("frcr_pchs_amt1") BigDecimal purchaseAmount,
	@JsonAlias("ovrs_stck_evlu_amt") BigDecimal evalAmount,
	@JsonAlias("evlu_pfls_rt") BigDecimal profitRate
) {
}
