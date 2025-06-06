package com.help.stockassistplatform.domain.news.service;

import static org.springframework.data.domain.Sort.Direction.*;

import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
		String tag,
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

		boolean hasTag = StringUtils.hasText(tag);
		boolean hasSentiment = sentiment.isPresent();
		log.info("Fetching news with tag: {}, sentiment: {}, page: {}, size: {}, lang: {}",
			tag, sentiment.map(Sentiment::getScore).orElse(null), pageable.getPageNumber(), pageable.getPageSize(),
			lang);

		if (hasTag && hasSentiment) {
			slice = newsRepository.findByTagAndAiAnalysis(tag, sentiment.get().getScore(), fixed);
		} else if (hasTag) {
			slice = newsRepository.findByTag(tag, fixed);
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
