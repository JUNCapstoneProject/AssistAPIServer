package com.help.stockassistplatform.domain.report.user.entity;

import com.help.stockassistplatform.domain.user.entity.User;
import com.help.stockassistplatform.global.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserReport extends BaseTimeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String category;
	private String title;

	@Lob
	@Column(nullable = false)
	private String description;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false)
	private String writerNickname;

	@Builder
	public UserReport(
		final String category,
		final String title,
		final String description,
		final User user,
		final String writerNickname
	) {
		this.category = category;
		this.title = title;
		this.description = description;
		this.user = user;
		this.writerNickname = writerNickname;
	}
}
