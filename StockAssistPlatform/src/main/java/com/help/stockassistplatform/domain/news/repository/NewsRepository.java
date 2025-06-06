package com.help.stockassistplatform.domain.news.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.help.stockassistplatform.domain.news.entity.NewsView;

@Repository
public interface NewsRepository extends JpaRepository<NewsView, String> {
	Slice<NewsView> findByTagIn(List<String> tags, Pageable pageable);

	Slice<NewsView> findAllBy(Pageable pageable);

	Slice<NewsView> findByAiAnalysis(Integer aiAnalysis, Pageable pageable);

	Slice<NewsView> findByTagAndAiAnalysis(String tag, Integer aiAnalysis, Pageable pageable);
}
