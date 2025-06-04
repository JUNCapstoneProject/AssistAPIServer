package com.help.stockassistplatform.domain.news.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

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
	private String aiAnalysis;

	public static NewsResponseDto from(final NewsView newsView) {
		final NewsResponseDto dto = new NewsResponseDto();
		dto.categories = List.of(
				new CategoryStatusDto(
						newsView.getTag(),
						toSentimentLabel(newsView.getAiAnalysis()),
						newsView.getAiAnalysis()
				)
		);
		dto.title = newsView.getTranslatedTitle();
		dto.description = summarize(newsView.getContent(), 100);
		dto.source = newsView.getOrganization();
		dto.date = formatDate(newsView.getPostedAt());
		dto.link = newsView.getUrl();
		dto.aiAnalysis = toSentimentLabel(newsView.getAiAnalysis());
		return dto;
	}

	private static String toSentimentLabel(Integer score) {
		if (null == score)
			return "분석 결과 없음";
		return switch (score) {
			case 2 -> "긍정";
			case 1 -> "중립";
			case 0 -> "부정";
			default -> "분석 결과 없음";
		};
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
