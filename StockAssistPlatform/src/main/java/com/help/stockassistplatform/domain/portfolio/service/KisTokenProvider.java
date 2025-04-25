package com.help.stockassistplatform.domain.portfolio.service;

import java.util.Map;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.help.stockassistplatform.domain.portfolio.dto.response.KisAuthResponse;
import com.help.stockassistplatform.global.config.properties.KisProperties;

import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class KisTokenProvider {

	private final WebClient koreaInvestWebClient;
	private final KisProperties kisProps;
	private static final String tokenUrl = "https://openapivts.koreainvestment.com:29443/oauth2/tokenP";

	public Mono<String> fetchAccessToken() {
		return koreaInvestWebClient.post()
			.uri(tokenUrl)
			.contentType(MediaType.APPLICATION_JSON)
			.bodyValue(Map.of(
				"grant_type", "client_credentials",
				"appkey", kisProps.appkey(),
				"appsecret", kisProps.appsecret()
			))
			.retrieve()
			.bodyToMono(KisAuthResponse.class)
			.map(KisAuthResponse::accessToken);
	}
}

