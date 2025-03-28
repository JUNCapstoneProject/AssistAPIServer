package com.help.stockassistplatform.domain.news.dto;

import java.util.List;

import org.springframework.data.domain.Slice;

public record NewsSliceResponse<T>(
	List<T> news,
	int currentPage,
	boolean hasNext
) {
	public static <T> NewsSliceResponse<T> from(final Slice<T> slice) {
		return new NewsSliceResponse<>(
			slice.getContent(),
			slice.getNumber() + 1, // 페이지 번호 1부터 시작
			slice.hasNext()
		);
	}
}
