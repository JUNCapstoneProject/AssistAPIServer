package com.help.stockassistplatform.domain.user.dto.response;

public record LoginResponseDto(
	String accessToken,
	String redirectUrl
) {
}
