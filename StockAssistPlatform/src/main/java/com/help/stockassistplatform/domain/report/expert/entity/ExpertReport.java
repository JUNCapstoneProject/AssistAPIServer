package com.help.stockassistplatform.domain.report.expert.entity;

import java.time.LocalDateTime;

import org.hibernate.annotations.Immutable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reports_vw")
@Immutable
@Getter
@NoArgsConstructor
public class ExpertReport {

	@Id
	@Column(name = "crawling_id")
	private String id;

	private String title;

	@Column(name = "transed_title")
	private String transedTitle;

	private Integer hits;
	private String author;

	@Column(name = "posted_at")
	private LocalDateTime date;

	@Column(columnDefinition = "TEXT")
	private String content;

	private String tag;

	@Column(name = "target_url")
	private String link;
}

