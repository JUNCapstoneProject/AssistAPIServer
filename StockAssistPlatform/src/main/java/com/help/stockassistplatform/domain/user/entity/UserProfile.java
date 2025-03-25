package com.help.stockassistplatform.domain.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(exclude = "user")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_profile")
@Entity
public class UserProfile {
	@Id
	private Long userId;

	@MapsId
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private User user;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false, length = 10)
	private String nickname;

	@Column(nullable = false)
	private String description = "";

	@Builder
	UserProfile(final User user, final String email, final String nickname) {
		this.user = user;
		userId = user.getUserId();
		this.email = email;
		this.nickname = nickname;
		description = "";
	}

	public void updateDescription(final String description) {
		this.description = description;
	}
}
