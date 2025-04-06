package com.help.stockassistplatform.domain.financial.dto.response;

import java.util.List;

public record FinancialListResponse(
        List<FinancialDetailResponse> financials,
        int currentPage,
        boolean hasNext
) {
    public static FinancialListResponse from(
            List<FinancialDetailResponse> financials,
            int currentPage,
            boolean hasNext
    ) {
        return new FinancialListResponse(financials, currentPage, hasNext);
    }
}