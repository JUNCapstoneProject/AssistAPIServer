package com.help.stockassistplatform.domain.macro.controller;

import com.help.stockassistplatform.domain.macro.dto.MacroResponse;
import com.help.stockassistplatform.domain.macro.service.MacroService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/economy")
public class MacroController {

    private final MacroService macroService;

    @GetMapping("/indicators")
    public ResponseEntity<MacroResponse> getMacroSummary() {
        List<String> indexKeys = List.of(
                "GDP",
                "REAL_GDP",
                "CPI",
                "PPI",
                "FEDFUNDS",
                "UNRATE",
                "TRADE_BALANCE",
                "DGS2",
                "PCE",
                "CCI",
                "PPI_VEHICLE",
                "PPI_ELECTRIC"
        );

        MacroResponse response = macroService.getMacroSummary(indexKeys); // 약어만 전달
        return ResponseEntity.ok(response);
    }
}