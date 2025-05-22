package com.help.stockassistplatform.domain.news.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Optional;

import com.help.stockassistplatform.domain.news.entity.NewsView;

import lombok.Getter;

@Getter
public class NewsResponseDto {
	private String category;
	private String status;
	private String title;
	private String description;
	private String source;
	private String date;
	private String link;

	public static NewsResponseDto from(final NewsView newsView) {
		final NewsResponseDto dto = new NewsResponseDto();
		dto.category = Optional.ofNullable(newsView.getTagsWithAnalysis()).orElse("기타");
		dto.status = newsView.getTagsWithAnalysis();
		dto.title = newsView.getTitle();
		dto.description = summarize(newsView.getContent(), 100);
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

	private static String summarize(final String text, final int maxLength) {
		if (null == text || text.length() <= maxLength)
			return text;
		final int lastSpace = text.lastIndexOf(" ", maxLength);
		if (-1 == lastSpace)
			return text.substring(0, maxLength) + "...";
		return text.substring(0, lastSpace) + "...";
	}
}
