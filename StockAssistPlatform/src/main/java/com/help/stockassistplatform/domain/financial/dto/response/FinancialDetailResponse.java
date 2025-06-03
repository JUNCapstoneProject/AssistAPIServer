package com.help.stockassistplatform.domain.financial.dto.response;

import java.math.BigDecimal;
import java.util.List;

public record FinancialDetailResponse(
        String name,
        String ticker,
        String price,
        Double change,
        Integer status,
        List<FinancialItemResponse> 손익계산서,
        List<FinancialItemResponse> 대차대조표,
        List<FinancialItemResponse> 현금흐름표,
        List<FinancialItemResponse> 주요비율
) {
    public static FinancialDetailResponse from(
            String name,
            String ticker,
            BigDecimal close,
            Float change,
            Integer status,
            List<FinancialItemResponse> income,
            List<FinancialItemResponse> balance,
            List<FinancialItemResponse> cash,
            List<FinancialItemResponse> ratios
    ) {
        return new FinancialDetailResponse(
                name,
                ticker,
                close == null ? "-" : close.toPlainString(),
                change == null ? 1 : Double.valueOf(change),
                status,
                income,
                balance,
                cash,
                ratios
        );
    }
}