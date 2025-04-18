package com.help.stockassistplatform.domain.portfolio;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class KisController {

	private final KisBalanceService balanceService;

	public KisController(KisBalanceService balanceService) {
		this.balanceService = balanceService;
	}

	@GetMapping("/balance")
	public ResponseEntity<?> getBalance(
		@RequestParam(defaultValue = "NASD") final String excgCd,
		@RequestParam(defaultValue = "USD") final String currency
	) {
		final StockBalanceResponse result = balanceService.getParsedBalance("50132199", "01", excgCd, currency);
		return ResponseEntity.ok(result);
	}
}
