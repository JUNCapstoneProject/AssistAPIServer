package com.help.stockassistplatform.domain.wishlist.dto;

import java.util.Locale;

import jakarta.validation.constraints.NotBlank;

public record WishlistRequestDto(
	@NotBlank(message = "티커는 공백일 수 없습니다.")
	String symbol
) {
	public String symbol() {
		return symbol.trim().toUpperCase(Locale.ROOT);
	}
}
