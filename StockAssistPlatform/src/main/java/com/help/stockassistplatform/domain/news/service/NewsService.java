package com.help.stockassistplatform.domain.news.service;

import static org.springframework.data.domain.Sort.Direction.*;

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
import com.help.stockassistplatform.domain.news.exception.NewsNotFoundException;
import com.help.stockassistplatform.domain.news.repository.NewsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {

	private final NewsRepository newsRepository;

	public Slice<NewsResponseDto> getNews(final String tag, final Pageable pageable, final Lang lang) {
		final Pageable fixed = PageRequest.of(
			pageable.getPageNumber(),
			pageable.getPageSize(),
			Sort.by(DESC, "postedAt").and(Sort.by(DESC, "id")));

		final Slice<NewsView> slice = (StringUtils.hasText(tag))
			? newsRepository.findByTag(tag, fixed)
			: newsRepository.findAllBy(fixed);

		if (slice.isEmpty())
			throw new NewsNotFoundException("뉴스 결과가 존재하지 않습니다.");

		return slice.map(view -> NewsResponseDto.from(view, lang));
	}
}
