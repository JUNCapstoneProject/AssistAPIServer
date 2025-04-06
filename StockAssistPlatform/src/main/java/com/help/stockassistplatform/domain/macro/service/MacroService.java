package com.help.stockassistplatform.domain.macro.service;

import com.help.stockassistplatform.domain.macro.dto.MacroResponse;

import java.util.List;

public interface MacroService {
    MacroResponse getMacroSummary(List<String> indexNames);
}