package com.help.stockassistplatform.domain.financial.dto.response;

public record FinancialItemResponse(
        String name,
        String value,
        Double change
) {
    public static FinancialItemResponse of(String name, String value, Double change) {
        return new FinancialItemResponse(name, value, change);
    }
}
