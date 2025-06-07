package com.help.stockassistplatform.domain.wishlist.entity;

import java.io.Serializable;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserWishlistId implements Serializable {
	@Column(name = "user_id", nullable = false, updatable = false)
	private UUID userId;

	@Column(name = "ticker", length = 10, nullable = false, updatable = false)
	private String ticker;

}
