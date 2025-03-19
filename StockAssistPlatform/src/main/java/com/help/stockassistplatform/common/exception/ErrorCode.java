package com.help.stockassistplatform.common.exception;

import lombok.Getter;

@Getter
public enum ErrorCode {
	INTERNAL_SERVER_ERROR(500, "서버 오류"),
	USER_NOT_FOUND(404, "사용자를 찾을 수 없습니다"),
	EMAIL_ALREADY_EXISTS(409, "이미 존재하는 이메일입니다");

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
