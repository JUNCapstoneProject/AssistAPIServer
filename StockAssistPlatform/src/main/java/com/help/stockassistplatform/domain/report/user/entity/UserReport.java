package com.help.stockassistplatform.domain.report.user.entity;

import java.util.UUID;

import org.hibernate.annotations.GenericGenerator;

import com.help.stockassistplatform.domain.user.entity.User;
import com.help.stockassistplatform.global.common.BaseTimeEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
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
	@GeneratedValue(generator = "uuid2")
	@GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
	@Column(name = "report_id", columnDefinition = "BINARY(16)")
	private UUID id;

	private String category;
	private String title;

	@Lob
	@Column(nullable = false)
	private String content;

	@ManyToOne(fetch = FetchType.LAZY, optional = false)
	@JoinColumn(name = "user_id")
	private User user;

	@Column(nullable = false, name = "user_nickname")
	private String writerNickname;

	@Builder
	public UserReport(
		final String category,
		final String title,
		final String content,
		final User user,
		final String writerNickname
	) {
		this.category = category;
		this.title = title;
		this.content = content;
		this.user = user;
		this.writerNickname = writerNickname;
	}

	public void update(final String title, final String content, final String category) {
		if (null != title && !title.isBlank()) {
			this.title = title;
		}
		if (null != content && !content.isBlank()) {
			this.content = content;
		}
		if (null != category && !category.isBlank()) {
			this.category = category;
		}
	}
}
