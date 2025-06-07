package com.help.stockassistplatform.domain.financial.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;

import com.help.stockassistplatform.domain.financial.service.FinancialService;
import com.help.stockassistplatform.global.common.response.ApiResponse;
import com.help.stockassistplatform.domain.financial.dto.response.FinancialDetailResponse;
import com.help.stockassistplatform.domain.financial.dto.response.FinancialListResponse;

@RestController
@RequestMapping("/api/financial")
@RequiredArgsConstructor
public class FinancialController {

    private final FinancialService financialService;

    @GetMapping
    public ApiResponse<?> getFinancial(
            @RequestParam(required = false) String ticker,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "3") int size,
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "desc") String sort,
            @RequestParam(required = false) Integer sentiment
    ) {
        if (ticker != null && !ticker.isBlank()) {
            FinancialDetailResponse detail = financialService.getDetailByTicker(ticker);
            return ApiResponse.success(detail);
        }

        FinancialListResponse list = financialService.getList(page, size, sortBy, sort, sentiment);
        return ApiResponse.success(list);
    }
}
