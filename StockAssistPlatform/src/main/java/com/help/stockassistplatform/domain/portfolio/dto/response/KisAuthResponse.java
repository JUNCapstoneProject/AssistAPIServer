package com.help.stockassistplatform.domain.portfolio.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public record KisAuthResponse(
	@JsonProperty("access_token") String accessToken
) {
}
