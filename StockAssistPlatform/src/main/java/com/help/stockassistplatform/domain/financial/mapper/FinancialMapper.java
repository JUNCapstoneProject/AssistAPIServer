package com.help.stockassistplatform.domain.financial.mapper;

import com.help.stockassistplatform.domain.financial.dto.response.FinancialItemResponse;
import com.help.stockassistplatform.domain.financial.entity.*;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

public class FinancialMapper {

    public static List<FinancialItemResponse> mapIncome(List<IncomeStatementView> list) {
        var cur = list.get(0);
        var prev = list.size() > 1 ? list.get(1) : null;

        return List.of(
                ofDollar("매출액", cur.getTotalRevenue(), prev != null ? prev.getTotalRevenue() : null),
                ofDollar("영업이익", cur.getOperatingIncome(), prev != null ? prev.getOperatingIncome() : null),
                ofDollar("순이익", cur.getNetIncome(), prev != null ? prev.getNetIncome() : null),
                ofDollar("EPS", cur.getEps(), prev != null ? prev.getEps() : null)
        );
    }

    public static List<FinancialItemResponse> mapBalance(List<BalanceSheetView> list) {
        var cur = list.get(0);
        var prev = list.size() > 1 ? list.get(1) : null;

        return List.of(
                ofDollar("총 자산", cur.getTotalAssets(), prev != null ? prev.getTotalAssets() : null),
                ofDollar("총 부채", cur.getTotalLiabilities(), prev != null ? prev.getTotalLiabilities() : null),
                ofDollar("자본금", cur.getStockholdersEquity(), prev != null ? prev.getStockholdersEquity() : null),
                ofPercent("부채비율", cur.getDebtRatio(), prev != null ? prev.getDebtRatio() : null)
        );
    }

    public static List<FinancialItemResponse> mapCash(List<CashFlowView> list) {
        var cur = list.get(0);
        var prev = list.size() > 1 ? list.get(1) : null;

        return List.of(
                ofDollar("영업활동현금흐름", cur.getOperatingCashFlow(), prev != null ? prev.getOperatingCashFlow() : null),
                ofDollar("투자활동현금흐름", cur.getInvestingCashFlow(), prev != null ? prev.getInvestingCashFlow() : null),
                ofDollar("재무활동현금흐름", cur.getFinancingCashFlow(), prev != null ? prev.getFinancingCashFlow() : null),
                ofDollar("잉여현금흐름", cur.getFreeCashFlow(), prev != null ? prev.getFreeCashFlow() : null)
        );
    }

    public static List<FinancialItemResponse> mapRatio(List<FinancialRatioView> list) {
        var cur = list.get(0);
        var prev = list.size() > 1 ? list.get(1) : null;

        return List.of(
                ofPercent("ROE", cur.getRoe(), prev != null ? prev.getRoe() : null),
                ofPercent("ROA", cur.getRoa(), prev != null ? prev.getRoa() : null),
                ofPercent("영업이익률", cur.getOperatingMargin(), prev != null ? prev.getOperatingMargin() : null),
                ofPercent("순이익률", cur.getNetProfitMargin(), prev != null ? prev.getNetProfitMargin() : null)
        );
    }

    private static FinancialItemResponse ofDollar(String name, BigDecimal recent, BigDecimal prev) {
        // ✅ fallback: 최신 데이터가 없으면 이전 데이터 사용
        BigDecimal valueToUse = recent != null ? recent : prev;

        if (valueToUse == null) return FinancialItemResponse.of(name, "-", null);

        BigDecimal thousand = new BigDecimal("1000");
        BigDecimal million = new BigDecimal("1000000");
        BigDecimal billion = new BigDecimal("1000000000");
        BigDecimal trillion = new BigDecimal("1000000000000");

        String value;
        if (valueToUse.compareTo(thousand) < 0) {
            value = valueToUse.setScale(2, RoundingMode.HALF_UP).toPlainString();
        } else if (valueToUse.compareTo(million) < 0) {
            value = valueToUse.divide(thousand, 2, RoundingMode.HALF_UP).toPlainString() + "K";
        } else if (valueToUse.compareTo(billion) < 0) {
            value = valueToUse.divide(million, 2, RoundingMode.HALF_UP).toPlainString() + "M";
        } else if (valueToUse.compareTo(trillion) < 0) {
            value = valueToUse.divide(billion, 2, RoundingMode.HALF_UP).toPlainString() + "B";
        } else {
            value = valueToUse.divide(trillion, 2, RoundingMode.HALF_UP).toPlainString() + "T";
        }

        // ✅ 최근 값이 없고 전분기 값으로 대체한 경우 "(전분기)" 표시 추가
        if (recent == null && prev != null) {
            value += " (전분기)";
        }

        Double change = calcChange(recent, prev);  // 변화율은 여전히 원래 recent 기준
        return FinancialItemResponse.of(name, value, change);
    }



    private static FinancialItemResponse ofPercent(String name, BigDecimal recent, BigDecimal prev) {
        BigDecimal valueToUse = recent != null ? recent : prev;

        String value;
        if (valueToUse != null) {
            value = valueToUse.setScale(2, RoundingMode.HALF_UP).toPlainString() + "%";
            if (recent == null && prev != null) {
                value += " (전분기)";
            }
        } else {
            value = "-";
        }

        Double change = calcChange(recent, prev);
        return FinancialItemResponse.of(name, value, change);
    }

    // ✅ 변화율 계산
    private static Double calcChange(BigDecimal recent, BigDecimal previous) {
        if (recent == null || previous == null || previous.compareTo(BigDecimal.ZERO) == 0) return null;

        return recent.divide(previous, 6, RoundingMode.HALF_UP)
                .subtract(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP)
                .doubleValue();
    }
}
