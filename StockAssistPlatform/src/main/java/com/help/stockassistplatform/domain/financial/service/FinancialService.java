package com.help.stockassistplatform.domain.financial.service;

import com.help.stockassistplatform.domain.financial.dto.response.FinancialDetailResponse;
import com.help.stockassistplatform.domain.financial.dto.response.FinancialListResponse;

public interface FinancialService {
    FinancialDetailResponse getDetailByTicker(String ticker);
    FinancialListResponse getListByPage(int page);
}