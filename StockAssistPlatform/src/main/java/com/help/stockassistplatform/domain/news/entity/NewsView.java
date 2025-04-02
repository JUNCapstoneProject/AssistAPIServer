package com.help.stockassistplatform.domain.news.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "news_vw")
@Immutable
@Getter
@NoArgsConstructor
public class NewsView {

	@Id
	@Column(name = "crawling_id")
	private String id;

	@Column(name = "organization")
	private String organization;

	@Column(name = "title")
	private String title;

	@Column(name = "hits")
	private Long hits;

	@Column(name = "author")
	private String author;

	@Column(name = "posted_at")
	private LocalDateTime postedAt;

	@Column(name = "content")
	private String content;

	@Column(name = "tag")
	private String tag;

	@Column(name = "ai_analysis")
	private String aiAnalysis;

	@Column(name = "target_url")
	private String url;
}
