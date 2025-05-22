package com.help.stockassistplatform.domain.news.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import com.help.stockassistplatform.domain.news.entity.NewsView;

import lombok.Getter;

@Getter
public class NewsResponseDto {
	private List<CategoryStatusDto> categories;
	private String title;
	private String description;
	private String source;
	private String date;
	private String link;

	public static NewsResponseDto from(final NewsView newsView) {
		final NewsResponseDto dto = new NewsResponseDto();
		dto.categories = parseCategories(newsView.getTagsWithAnalysis());
		dto.title = newsView.getTitle();
		dto.description = summarize(newsView.getContent(), 100);
		dto.source = newsView.getOrganization();
		dto.date = formatDate(newsView.getPostedAt());
		dto.link = newsView.getUrl();
		return dto;
	}

	private static List<CategoryStatusDto> parseCategories(final String tagsWithAnalysis) {
		if (null == tagsWithAnalysis || tagsWithAnalysis.isBlank()) {
			return Collections.emptyList();
		}
		
		return Arrays.stream(tagsWithAnalysis.split(","))
			.map(String::trim)
			.filter(entry -> entry.contains(":"))
			.map(NewsResponseDto::toCategoryStatusDto)
			.toList();
	}

	private static CategoryStatusDto toCategoryStatusDto(final String entry) {
		final String[] parts = entry.split(":", 2);
		final String name = parts[0];
		final String status = (parts.length > 1 && !parts[1].isBlank()) ? parts[1] : "분석 결과 없음";
		return new CategoryStatusDto(name, status);
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
