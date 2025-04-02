package com.help.stockassistplatform.domain.news.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import com.help.stockassistplatform.domain.news.entity.NewsView;

public class NewsResponseDto {
	private String category;
	private String status;
	private String title;
	private String description;
	private String source;
	private String date;
	private String link;
	/*
	*   "category": "테슬라",
        "status": "긍정",
        "title": "테슬라, 자율주행 기술 업데이트로 주가 상승",
        "description": "테슬라가 최신 자율주행 소프트웨어 업데이트를 발표하며 주가가 3% 상승했습니다.",
        "source": "블룸버그",
        "date": "2024.03.08",
        "link": "https://example.com/news1"
	* */

	public static NewsResponseDto from(final NewsView newsView) {
		final NewsResponseDto dto = new NewsResponseDto();
		dto.category = Optional.ofNullable(newsView.getTag()).orElse("기타");
		dto.status = newsView.getAiAnalysis();
		dto.title = newsView.getTitle();
		dto.description = newsView.getContent();
		dto.source = newsView.getOrganization();
		dto.date = formatDate(newsView.getPostedAt());
		dto.link = newsView.getUrl();
		return dto;
	}

	private static String formatDate(final LocalDateTime dateTime) {
		if (null == dateTime) {
			return "";
		}
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd", Locale.KOREA);
		return dateTime.format(formatter);
	}
}
