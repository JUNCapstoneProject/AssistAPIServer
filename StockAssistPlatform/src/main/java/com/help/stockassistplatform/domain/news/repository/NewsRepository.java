package com.help.stockassistplatform.domain.news.repository;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.help.stockassistplatform.domain.news.entity.NewsView;

@Repository
public interface NewsRepository extends JpaRepository<NewsView, String> {

	Slice<NewsView> findByTag(String tag, Pageable pageable);
	Slice<NewsView> findByTagAndAiAnalysis(String tag, Integer aiAnalysis, Pageable pageable);

	Slice<NewsView> findByTagIn(List<String> tags, Pageable pageable);
	Slice<NewsView> findByTagInAndAiAnalysis(List<String> tags, Integer aiAnalysis, Pageable pageable); // ← 이거 꼭 추가

	// 감성 필터만
	Slice<NewsView> findByAiAnalysis(Integer aiAnalysis, Pageable pageable);

	// 전체 조회
	Slice<NewsView> findAllBy(Pageable pageable);
}
