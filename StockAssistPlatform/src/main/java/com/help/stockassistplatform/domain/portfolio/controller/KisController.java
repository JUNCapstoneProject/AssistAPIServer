package com.help.stockassistplatform.domain.portfolio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.domain.portfolio.dto.StockBalanceResponse;
import com.help.stockassistplatform.domain.portfolio.service.KisBalanceService;
import com.help.stockassistplatform.global.config.properties.KisProperties;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class KisController {

	private final KisBalanceService balanceService;
	private final KisProperties kisProp;

	@GetMapping("/portfolio")
	public ResponseEntity<?> getBalance(
		@RequestParam(defaultValue = "NASD") final String excgCd,
		@RequestParam(defaultValue = "USD") final String currency
	) {
		final StockBalanceResponse result = balanceService.getParsedBalance(excgCd, currency);
		return ResponseEntity.ok(result);
	}
}
