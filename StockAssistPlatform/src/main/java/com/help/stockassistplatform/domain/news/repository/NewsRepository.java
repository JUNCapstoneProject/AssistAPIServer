package com.help.stockassistplatform.domain.news.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.help.stockassistplatform.domain.news.entity.NewsView;

@Repository
public interface NewsRepository extends JpaRepository<NewsView, String> {
	Slice<NewsView> findByTag(String tag, Pageable pageable);

	Slice<NewsView> findAllBy(Pageable pageable);
}
