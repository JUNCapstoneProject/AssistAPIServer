package com.help.stockassistplatform.domain.news.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.help.stockassistplatform.domain.news.entity.Lang;
import com.help.stockassistplatform.domain.news.entity.NewsView;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NewsResponseDto {
	private List<CategoryStatusDto> categories;
	private String title;
	private String description;
	private String source;
	private String date;
	private String link;

	public static NewsResponseDto from(final NewsView view, final Lang lang) {
		return NewsResponseDto.builder()
			.categories(List.of(new CategoryStatusDto(
				view.getTag(),
				toSentimentLabel(view.getAiAnalysis()),
				view.getAiAnalysis())))
			.title(resolveTitle(view, lang))
			.description(summarize(view.getContent(), 100))
			.source(view.getOrganization())
			.date(formatDate(view.getPostedAt()))
			.link(view.getUrl())
			.build();
	}

	private static String resolveTitle(final NewsView view, final Lang lang) {
		return switch (lang) {
			case EN -> view.getTitle();
			case KO -> Optional.ofNullable(view.getTranslatedTitle())
				.orElse(view.getTitle());
		};
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
