package com.help.stockassistplatform.domain.wishlist.entity;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.Locale;

import com.help.stockassistplatform.domain.user.entity.User;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(
	name = "user_wishlist",
	uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "ticker"})
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserWishlist {
	@EmbeddedId
	private UserWishlistId id;

	@MapsId("userId")
	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	private User user;

	@Column(name = "added_at", nullable = false)
	private LocalDateTime addedAt;

	private UserWishlist(User user, String ticker) {
		this.id = new UserWishlistId(user.getUserId(), ticker);
		this.user = user;
		this.addedAt = OffsetDateTime.now().toLocalDateTime();
	}

	public static UserWishlist of(final User user, final String ticker) {
		return new UserWishlist(user, ticker.toUpperCase(Locale.ROOT));
	}
}
