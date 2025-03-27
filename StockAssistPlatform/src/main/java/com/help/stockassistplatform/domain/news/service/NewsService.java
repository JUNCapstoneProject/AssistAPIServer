package com.help.stockassistplatform.domain.news.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.news.dto.NewsResponseDto;
import com.help.stockassistplatform.domain.news.entity.NewsView;
import com.help.stockassistplatform.domain.news.repository.NewsRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class NewsService {

	private final NewsRepository newsRepository;

	public Slice<NewsResponseDto> getNews(final String tag, final Pageable pageable) {
		final Slice<NewsView> slice;

		if (null == tag || tag.isBlank()) {
			slice = newsRepository.findAllBy(pageable);
		} else {
			slice = newsRepository.findByTags(tag, pageable);
		}

		return slice.map(NewsResponseDto::from);
	}
}
