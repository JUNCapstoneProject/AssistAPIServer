package com.help.stockassistplatform.domain.portfolio;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.StreamSupport;

import com.fasterxml.jackson.databind.JsonNode;

public record StockBalanceResponse(
	String totalAsset,
	String investmentAmount,
	String evaluationProfit,
	String profitRate,
	String cash,
	List<StockItem> stocks
) {
	private static final String DEFAULT_AMOUNT = "0";
	private static final String DEFAULT_RATE = "0";
	private static final String DEFAULT_CODE = "N/A";
	private static final String DEFAULT_NAME = "종목명없음";

	public static StockBalanceResponse from(
		final JsonNode output1,
		final JsonNode output2,
		final BigDecimal cash
	) {
		final List<StockItem> stockItems = StreamSupport.stream(output1.spliterator(), false)
			.map(StockItem::from)
			.toList();

		final BigDecimal investmentAmount = getDecimalSafe(output2, "frcr_buy_amt_smtl1", DEFAULT_AMOUNT); // 투자금액
		final BigDecimal evaluationProfit = getDecimalSafe(output2, "tot_evlu_pfls_amt", DEFAULT_AMOUNT); // 평가손익
		final BigDecimal profitRate = getDecimalSafe(output2, "tot_pftrt", DEFAULT_RATE);
		final BigDecimal totalAsset = investmentAmount
			.add(evaluationProfit)
			.add(cash);
		
		return new StockBalanceResponse(
			totalAsset.toPlainString(),
			investmentAmount.toPlainString(),
			evaluationProfit.toPlainString(),
			profitRate.toPlainString(),
			cash.toPlainString(),
			stockItems
		);
	}

	private static BigDecimal getEvalAmountSafe(final String value) {
		try {
			return new BigDecimal(value);
		} catch (NumberFormatException e) {
			return new BigDecimal(DEFAULT_AMOUNT);
		}
	}

	private static String getSafe(final JsonNode node, final String field, final String defaultValue) {
		if (null == node || !node.has(field) || node.get(field).isNull())
			return defaultValue;
		final String value = node.get(field).asText();
		return value.isEmpty() ? defaultValue : value;
	}

	static BigDecimal getDecimalSafe(final JsonNode node, final String field, final String defaultValue) {
		try {
			return new BigDecimal(getSafe(node, field, defaultValue));
		} catch (final NumberFormatException e) {
			return new BigDecimal(defaultValue);
		}
	}

	public static record StockItem(
		String symbol,
		String name,
		String currentPrice,
		String evalProfit,
		String quantity,
		String purchaseAmount,
		String evalAmount,
		String profitRate
	) {
		public static StockItem from(final JsonNode node) {
			return new StockItem(
				getSafe(node, "ovrs_pdno", DEFAULT_CODE),
				getSafe(node, "ovrs_item_name", DEFAULT_NAME),
				getSafe(node, "now_pric2", DEFAULT_AMOUNT),
				getSafe(node, "frcr_evlu_pfls_amt", DEFAULT_AMOUNT),
				getSafe(node, "ovrs_cblc_qty", DEFAULT_AMOUNT),
				getSafe(node, "frcr_pchs_amt1", DEFAULT_AMOUNT),
				getSafe(node, "ovrs_stck_evlu_amt", DEFAULT_AMOUNT),
				getSafe(node, "evlu_pfls_rt", DEFAULT_RATE)
			);
		}
	}
}

