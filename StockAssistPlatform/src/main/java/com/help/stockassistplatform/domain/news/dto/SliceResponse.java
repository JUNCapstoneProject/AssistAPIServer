package com.help.stockassistplatform.domain.news.dto;

import java.util.List;

import org.springframework.data.domain.Slice;

public record SliceResponse<T>(
	List<T> news,
	int totalItems,
	int currentPage,
	int limit,
	boolean hasNext
) {
	public static <T> SliceResponse<T> from(final Slice<T> slice) {
		return new SliceResponse<>(
			slice.getContent(),
			slice.getNumberOfElements(),
			slice.getNumber() + 1, // 페이지 번호 1부터 시작
			slice.getSize(),
			slice.hasNext()
		);
	}
}
