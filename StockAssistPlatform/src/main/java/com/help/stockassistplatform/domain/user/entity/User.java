package com.help.stockassistplatform.domain.user.entity;

import java.time.LocalDateTime;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString(exclude = "userProfile")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "user_entity")
@EntityListeners(AuditingEntityListener.class)
@Entity
public class User {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long userId;

	@Column(nullable = false, unique = true)
	private String email;

	@Column(nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private UserRole role = UserRole.USER;

	@Column(nullable = false, updatable = false)
	@CreatedDate
	private LocalDateTime createdAt;

	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
	private UserProfile userProfile;

	@Builder
	public User(final String email, final String password, final String nickname) {
		this.email = email;
		this.password = password;
		userProfile = new UserProfile(this, email, nickname);
	}
}
