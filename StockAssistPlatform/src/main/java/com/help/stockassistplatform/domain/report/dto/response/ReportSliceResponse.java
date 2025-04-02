package com.help.stockassistplatform.domain.report.dto.response;

import java.util.List;

import org.springframework.data.domain.Slice;

public record ReportSliceResponse<T>(
	List<T> reports,
	int currentPage,
	boolean hasNext
) {
	public static <T> ReportSliceResponse<T> from(final Slice<T> slice) {
		return new ReportSliceResponse<>(
			slice.getContent(),
			slice.getNumber() + 1, // 페이지 번호 1부터 시작
			slice.hasNext()
		);
	}
}
