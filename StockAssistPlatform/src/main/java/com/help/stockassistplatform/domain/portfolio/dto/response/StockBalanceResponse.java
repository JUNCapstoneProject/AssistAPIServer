package com.help.stockassistplatform.domain.portfolio.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record StockBalanceResponse(
	String totalAsset,
	String investmentAmount,
	String evaluationProfit,
	String profitRate,
	String cash,
	List<KisStockItem> stocks
) {
	public static StockBalanceResponse from(
		final KisStockSummary summary,
		final List<KisStockItem> stocks,
		final BigDecimal cash
	) {
		final BigDecimal totalAsset = summary.investmentAmount()
			.add(summary.evaluationProfit())
			.add(cash);

		return new StockBalanceResponse(
			totalAsset.toPlainString(),
			summary.investmentAmount().toPlainString(),
			summary.evaluationProfit().toPlainString(),
			summary.profitRate().toPlainString(),
			cash.toPlainString(),
			stocks
		);
	}
}
