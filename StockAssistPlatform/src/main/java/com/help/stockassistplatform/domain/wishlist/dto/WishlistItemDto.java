package com.help.stockassistplatform.domain.wishlist.dto;

public record WishlistItemDto(
	String symbol,
	String name,
	String price,
	boolean wished
) {
}
