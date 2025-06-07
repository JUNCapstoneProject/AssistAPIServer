package com.help.stockassistplatform.domain.wishlist.dto;

public record WishlistItemDto(
	String ticker,
	String name,
	String currentPrice,
	boolean wished
) {
}
