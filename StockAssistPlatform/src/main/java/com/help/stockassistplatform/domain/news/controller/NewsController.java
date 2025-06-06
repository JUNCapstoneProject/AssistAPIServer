package com.help.stockassistplatform.domain.news.controller;

import static org.springframework.data.domain.Sort.Direction.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.domain.news.dto.NewsResponseDto;
import com.help.stockassistplatform.domain.news.dto.NewsSliceResponse;
import com.help.stockassistplatform.domain.news.entity.Lang;
import com.help.stockassistplatform.domain.news.entity.Sentiment;
import com.help.stockassistplatform.domain.news.service.NewsService;
import com.help.stockassistplatform.global.common.response.ApiResponse;

import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;

@Validated
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class NewsController {
	private final NewsService newsService;

	@GetMapping("/news")
	public ApiResponse<?> getNews(
                @RequestParam(required = false) final List<String> category,
		@RequestParam(required = false) final String sentiment,
		@RequestParam(defaultValue = "1") @Min(1L) final int page,
		@RequestParam(defaultValue = "6") @Min(1L) final int limit,
		@RequestParam(defaultValue = "ko") final String lang
	) {
		final Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(DESC, "postedAt"));
		final Slice<NewsResponseDto> result = newsService.getNews(
			category,
			Sentiment.of(sentiment),
			pageable,
			Lang.of(lang)
		);
		return ApiResponse.success(NewsSliceResponse.from(result));
	}
}
