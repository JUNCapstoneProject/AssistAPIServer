package com.help.stockassistplatform.domain.news.controller;

import static org.springframework.data.domain.Sort.Direction.*;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.domain.news.dto.NewsResponseDto;
import com.help.stockassistplatform.domain.news.dto.SliceResponse;
import com.help.stockassistplatform.domain.news.service.NewsService;
import com.help.stockassistplatform.global.common.response.ApiResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class NewsController {
	private final NewsService newsService;

	@GetMapping("/news")
	public ApiResponse<?> getNews(
		@RequestParam(required = false) final String category,
		@RequestParam(defaultValue = "1") final int page,
		@RequestParam(defaultValue = "20") final int limit
	) {
		final Pageable pageable = PageRequest.of(page - 1, limit, Sort.by(DESC, "postedAt"));
		final Slice<NewsResponseDto> result = newsService.getNews(category, pageable);
		return ApiResponse.success(SliceResponse.from(result));
	}
}
