package com.help.stockassistplatform.domain.financial.service;

import java.util.UUID;

import com.help.stockassistplatform.domain.financial.dto.response.FinancialDetailResponse;
import com.help.stockassistplatform.domain.financial.dto.response.FinancialListResponse;

import jakarta.annotation.Nullable;

public interface FinancialService {

	/**
	 * 단일 종목의 재무 상세 정보를 반환
	 */
	FinancialDetailResponse getDetailByTicker(String ticker, @Nullable UUID userId);

	/**
	 * (기존) 단순 페이지 조회
	 */
	FinancialListResponse getListByPage(int page, @Nullable UUID userId);

	/**
	 * (신규) 정렬, 필터링을 포함한 재무 목록 조회
	 *
	 * @param page      페이지 번호 (1부터 시작)
	 * @param size      페이지 크기
	 * @param sortBy    정렬 기준 (예: "price", "revenue")
	 * @param sort      정렬 방향 ("asc", "desc")
	 * @param sentiment AI 분석 결과 필터링 (예: 1, 0, -1 등)
	 */
	FinancialListResponse getList(int page, int size, String sortBy, String sort, Integer sentiment,
		@Nullable UUID userId);
}
