package com.help.stockassistplatform.domain.financial.service.impl;

import com.help.stockassistplatform.domain.financial.dto.response.*;
import com.help.stockassistplatform.domain.financial.entity.*;
import com.help.stockassistplatform.domain.financial.repository.*;
import com.help.stockassistplatform.domain.financial.service.*;
import com.help.stockassistplatform.domain.financial.mapper.FinancialMapper;
import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FinancialServiceImpl implements FinancialService {

    private final StockPriceViewRepository stockPriceViewRepository;
    private final FinancialAnalysisViewRepository analysisRepository;
    private final IncomeStatementViewRepository incomeRepository;
    private final BalanceSheetViewRepository balanceRepository;
    private final CashFlowViewRepository cashRepository;
    private final FinancialRatioViewRepository ratioRepository;

    private static final int PAGE_SIZE = 3;

    @Override
    public FinancialDetailResponse getDetailByTicker(String ticker) {
        // ✅ 종목명, 티커, 종가, 등락률 → 모두 StockPriceView에서 가져옴
        StockPriceView price = stockPriceViewRepository.findTop1ByTickerOrderByPostedAtDesc(ticker)
                .orElseThrow(() -> new CustomException(ErrorCode.TICKER_NOT_FOUND));

        String name = price.getName(); // 종목명 (한글)
        BigDecimal close = price.getClose();
        Float change = price.getChange();

        // ✅ AI 분석 상태
        String status = analysisRepository.findTop1ByCompanyOrderByPostedAtDesc(ticker)
                .map(FinancialAnalysisView::getAiAnalysis)
                .orElse("중립");

        // ✅ 재무제표 각 항목 조회
        List<IncomeStatementView> incomeViews = incomeRepository.findTop2ByCompanyOrderByPostedAtDesc(ticker);
        List<BalanceSheetView> balanceViews = balanceRepository.findTop2ByCompanyOrderByPostedAtDesc(ticker);
        List<CashFlowView> cashViews = cashRepository.findTop2ByCompanyOrderByPostedAtDesc(ticker);
        List<FinancialRatioView> ratioViews = ratioRepository.findTop2ByCompanyOrderByPostedAtDesc(ticker);

        List<FinancialItemResponse> incomeList = FinancialMapper.mapIncome(incomeViews);
        List<FinancialItemResponse> balanceList = FinancialMapper.mapBalance(balanceViews);
        List<FinancialItemResponse> cashList = FinancialMapper.mapCash(cashViews);
        List<FinancialItemResponse> ratioList = FinancialMapper.mapRatio(ratioViews);

        return FinancialDetailResponse.from(
                name,
                ticker,
                close,
                change,
                status,
                incomeList,
                balanceList,
                cashList,
                ratioList
        );
    }

    @Override
    public FinancialListResponse getListByPage(int page) {
        PageRequest pageable = PageRequest.of(page - 1, PAGE_SIZE);

        List<StockPriceView> stocks = stockPriceViewRepository.findAllGroupedByTicker(pageable);
        List<FinancialDetailResponse> financials = new ArrayList<>();

        for (StockPriceView stock : stocks) {
            try {
                financials.add(getDetailByTicker(stock.getTicker()));
            } catch (Exception e) {
                // 생략 or logging only
            }
        }

        if (financials.isEmpty()) {
            throw new CustomException(ErrorCode.TICKER_NOT_FOUND); // or NEW ENUM like ErrorCode.EMPTY_PAGE
        }

        long totalCount = stockPriceViewRepository.countDistinctTicker(); // 커스텀 메서드 필요
        boolean hasNext = totalCount > page * PAGE_SIZE;

        return FinancialListResponse.from(financials, page, hasNext);
    }
}