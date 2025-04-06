package com.help.stockassistplatform.domain.macro.dto;

import java.math.BigDecimal;
import java.util.Map;

public record MacroResponse(
        boolean success,
        Map<String, MacroDTO> response,
        String error
) {
    public record MacroDTO(
            BigDecimal value,
            BigDecimal change
    ) {}
}