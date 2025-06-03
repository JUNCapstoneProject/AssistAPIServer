package com.help.stockassistplatform.global.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	INVALID_CREDENTIALS(400, "아이디나 패스워드가 일치하지 않습니다."),
	INVALID_PASSWORD(400, "잘못된 비밀번호입니다"),

	EXPIRED_TOKEN(401, "인증 토큰이 만료되었습니다"),
	INVALID_TOKEN(401, "유효하지 않은 토큰입니다"),
	UNAUTHORIZED(401, "로그인이 필요합니다"),
	UNAUTHORIZED_PASSWORD_CHANGE(401, "비밀번호 변경 권한이 없습니다"),
	UNTRUSTED_EMAIL_SERVER(403, "신뢰할 수 없는 이메일 서버입니다"),

	USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다"),
	NOT_FOUND(404, "페이지를 찾을 수 없습니다"),
	TICKER_NOT_FOUND(404, "재무제표 데이터를 찾을 수 없습니다"),
	PAGE_OUT_OF_RANGE(404, "요청한 페이지는 존재하지 않습니다"),

	EMAIL_ALREADY_EXISTS(409, "이미 존재하는 이메일입니다"),

	JSON_PARSING_FAILURE(500, "JSON 매핑에 실패했습니다"),
	KIS_ACCESS_TOKEN_FAILURE(500, "KIS Access Token 획득 실패"),
	KIS_DEPOSIT_QUERY_FAILURE(500, "KIS API 예수금 조회 실패"),
	INTERNAL_SERVER_ERROR(500, "서버에서 오류가 발생했습니다");

	private final int status;
	private final String message;

	ErrorCode(final int status, final String message) {
		this.status = status;
		this.message = message;
	}

	@Override
	public String toString() {
		return message;
	}
}
