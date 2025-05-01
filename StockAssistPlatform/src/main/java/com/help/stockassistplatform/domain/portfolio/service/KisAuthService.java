package com.help.stockassistplatform.domain.portfolio.service;

import java.time.Duration;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class KisAuthService {

	private final RedisTemplate<String, String> redisTemplate;
	private final KisTokenProvider tokenProvider;

	private static final String KIS_TOKEN_KEY = "KIS_ACCESS_TOKEN";
	private static final Duration TOKEN_TTL = Duration.ofHours(6); // 만료 여유 고려

	public String getAccessToken() {
		final String cachedToken = redisTemplate.opsForValue().get(KIS_TOKEN_KEY);
		if (cachedToken != null)
			return cachedToken;

		final String accessToken = tokenProvider.fetchAccessToken().block();
		redisTemplate.opsForValue().set(KIS_TOKEN_KEY, accessToken, TOKEN_TTL);
		return accessToken;
	}
}
