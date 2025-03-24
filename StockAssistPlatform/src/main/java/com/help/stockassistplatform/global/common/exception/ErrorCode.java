package com.help.stockassistplatform.global.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	INTERNAL_SERVER_ERROR(500, "서버에서 오류가 발생했습니다"),
	USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다"),
	EMAIL_ALREADY_EXISTS(409, "이미 존재하는 이메일입니다"),
	EXPIRED_TOKEN(401, "인증 토큰이 만료되었습니다"),
	INVALID_TOKEN(401, "유효하지 않은 토큰입니다"),
	UNAUTHORIZED(401, "로그인이 필요합니다"),
	INVALID_CREDENTIALS(401, "아이디나 비밀번호가 잘못되었습니다"),
	NOT_FOUND(404, "페이지를 찾을 수 없습니다");

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
