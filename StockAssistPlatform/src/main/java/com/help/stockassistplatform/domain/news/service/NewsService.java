package com.help.stockassistplatform.domain.news.service;

import static org.springframework.data.domain.Sort.Direction.*;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.news.dto.NewsResponseDto;
import com.help.stockassistplatform.domain.news.entity.Lang;
import com.help.stockassistplatform.domain.news.entity.NewsView;
import com.help.stockassistplatform.domain.news.entity.Sentiment;
import com.help.stockassistplatform.domain.news.exception.NewsNotFoundException;
import com.help.stockassistplatform.domain.news.repository.NewsRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {

	private final NewsRepository newsRepository;

	public Slice<NewsResponseDto> getNews(
		List<String> tags,
		Optional<Sentiment> sentiment,
		Pageable pageable,
		Lang lang
	) {
		final Pageable fixed = PageRequest.of(
			pageable.getPageNumber(),
			pageable.getPageSize(),
			Sort.by(DESC, "postedAt").and(Sort.by(DESC, "id"))
		);

		final Slice<NewsView> slice;

		boolean hasTags = tags != null && !tags.isEmpty();
		boolean hasSentiment = sentiment.isPresent();

		log.info("Fetching news with tags: {}, sentiment: {}, page: {}, size: {}, lang: {}",
			tags, sentiment.map(Sentiment::getScore).orElse(null),
			pageable.getPageNumber(), pageable.getPageSize(), lang);

		if (hasTags && hasSentiment) {
			slice = newsRepository.findByTagInAndAiAnalysis(tags, sentiment.get().getScore(), fixed);
		} else if (hasTags) {
			slice = newsRepository.findByTagIn(tags, fixed);
		} else if (hasSentiment) {
			slice = newsRepository.findByAiAnalysis(sentiment.get().getScore(), fixed);
		} else {
			slice = newsRepository.findAllBy(fixed);
		}

		if (slice.isEmpty())
			throw new NewsNotFoundException("뉴스 결과가 존재하지 않습니다.");

		return slice.map(v -> NewsResponseDto.from(v, lang));
	}
}
