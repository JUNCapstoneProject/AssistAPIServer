package com.help.stockassistplatform.domain.macro.service.impl;

import com.help.stockassistplatform.domain.macro.dto.MacroResponse;
import com.help.stockassistplatform.domain.macro.dto.MacroResponse.MacroDTO;
import com.help.stockassistplatform.domain.macro.entity.MacroView;
import com.help.stockassistplatform.domain.macro.repository.MacroViewRepository;
import com.help.stockassistplatform.domain.macro.service.MacroService;
import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MacroServiceImpl implements MacroService {

    private final MacroViewRepository macroViewRepository;

    private static final Map<String, String> INDEX_NAME_MAP = Map.ofEntries(
            Map.entry("GDP", "Nominal GDP"),
            Map.entry("REAL_GDP", "Real GDP"),
            Map.entry("CPI", "Consumer Price Index (CPI)"),
            Map.entry("PPI", "Producer Price Index (PPI)"),
            Map.entry("FEDFUNDS", "Federal Funds Rate"),
            Map.entry("UNRATE", "Unemployment Rate"),
            Map.entry("TRADE_BALANCE", "Trade Balance"),
            Map.entry("DGS2", "2-Year Treasury Yield"),
            Map.entry("PCE", "Personal Consumption Expenditures (PCE)"),
            Map.entry("CCI", "Consumer Confidence Index (CCI)"),
            Map.entry("PPI_VEHICLE", "PPI - Vehicle"),
            Map.entry("PPI_ELECTRIC", "PPI - Electric")
    );

    @Override
    public MacroResponse getMacroSummary(List<String> indexKeys) {
        Map<String, MacroDTO> responseMap = new LinkedHashMap<>();

        for (String key : indexKeys) {
            String indexName = INDEX_NAME_MAP.get(key);
            if (indexName == null) continue;

            List<MacroView> views = macroViewRepository.findTop2ByIndexNameOrderByPostedAtDesc(indexName);

            if (views.isEmpty()) {
                throw new CustomException(ErrorCode.NOT_FOUND);
            }

            MacroView latest = views.get(0);
            BigDecimal value = latest.getIndexValue();
            BigDecimal previous = views.size() > 1 ? views.get(1).getIndexValue() : null;

            BigDecimal change = calcChange(value, previous);

            responseMap.put(key, new MacroDTO(value, change));
        }

        return new MacroResponse(true, responseMap, null);
    }

    private static BigDecimal calcChange(BigDecimal recent, BigDecimal previous) {
        if (recent == null || previous == null || previous.compareTo(BigDecimal.ZERO) == 0) {
            return null;
        }

        return recent.divide(previous, 6, RoundingMode.HALF_UP)
                .subtract(BigDecimal.ONE)
                .multiply(BigDecimal.valueOf(100))
                .setScale(2, RoundingMode.HALF_UP);
    }
}