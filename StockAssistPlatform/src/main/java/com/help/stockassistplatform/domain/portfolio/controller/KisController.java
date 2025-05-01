package com.help.stockassistplatform.domain.portfolio.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.domain.portfolio.dto.response.StockBalanceResponse;
import com.help.stockassistplatform.domain.portfolio.service.KisBalanceService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class KisController {

	private final KisBalanceService balanceService;

	/**
	 * KIS API를 통해 주식 잔고를 조회합니다.
	 *
	 * @param excgCd 거래소 코드 (default: NASD)
	 * @param currency 통화 코드 (default: USD)
	 * @return 주식 잔고 정보
	 */
	@GetMapping("/portfolio")
	public ResponseEntity<?> getBalance(
		@RequestParam(defaultValue = "NASD") final String excgCd,
		@RequestParam(defaultValue = "USD") final String currency
	) {
		final StockBalanceResponse result = balanceService.getParsedBalance(excgCd, currency);
		return ResponseEntity.ok(result);
	}
}
