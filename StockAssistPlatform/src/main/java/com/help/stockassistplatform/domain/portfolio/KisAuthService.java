package com.help.stockassistplatform.domain.portfolio;

import java.time.Duration;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KisAuthService {

	private final RestTemplate restTemplate = new RestTemplate();
	private final RedisTemplate<String, String> redisTemplate;

	@Value("${kis.app-key}")
	private String appKey;

	@Value("${kis.app-secret}")
	private String appSecret;

	private static final String KIS_TOKEN_KEY = "KIS_ACCESS_TOKEN";
	private static final Duration TOKEN_TTL = Duration.ofHours(6); // 만료 여유 고려

	public String getAccessToken() {
		// Redis에서 토큰 조회
		String cachedToken = redisTemplate.opsForValue().get(KIS_TOKEN_KEY);
		if (cachedToken != null) {
			return cachedToken;
		}

		// 토큰 발급 요청
		String url = "https://openapivts.koreainvestment.com:29443/oauth2/tokenP";

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		Map<String, String> body = Map.of(
			"grant_type", "client_credentials",
			"appkey", appKey,
			"appsecret", appSecret
		);

		HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

		ResponseEntity<JsonNode> response = restTemplate.postForEntity(url, request, JsonNode.class);
		if (!response.getStatusCode().is2xxSuccessful()) {
			throw new RuntimeException("KIS 인증 실패: " + response.getStatusCode());
		}

		String accessToken = response.getBody().get("access_token").asText();

		// Redis에 저장
		redisTemplate.opsForValue().set(KIS_TOKEN_KEY, accessToken, TOKEN_TTL);

		return accessToken;
	}
}
