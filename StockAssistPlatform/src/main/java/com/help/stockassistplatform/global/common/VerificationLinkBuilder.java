package com.help.stockassistplatform.global.common;

import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class VerificationLinkBuilder {

	public String registerUriBuild(final String baseUrl, final String token) {
		return UriComponentsBuilder.fromUriString(baseUrl)
			.path("/verify")
			.queryParam("token", token)
			.build()
			.toUriString();
	}

	public String passwordResetUriBuild(final String baseUrl, final String token) {
		return UriComponentsBuilder.fromUriString(baseUrl)
			.path("/reset-password")
			.queryParam("token", token)
			.build()
			.toUriString();
	}
}
