package com.help.stockassistplatform.domain.user.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.stockassistplatform.domain.user.dto.SignupRequest;
import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthTokenService {
	private static final long EXPIRE_MINUTES = 10L;
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	public String createToken() {
		return UUID.randomUUID().toString();
	}

	public void saveUserInfoToRedis(final String token, final SignupRequest request) {
		try {
			final String userJson = objectMapper.writeValueAsString(request);
			redisTemplate.opsForValue().set(token, userJson, Duration.ofMinutes(EXPIRE_MINUTES));
		} catch (final JsonProcessingException e) {
			log.error("Serialization Error: ", e);
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	public SignupRequest getUserInfoFromRedis(final String token) {
		final String userJson = (String)redisTemplate.opsForValue().get(token);
		if (userJson == null) {
			throw new CustomException(ErrorCode.EXPIRED_TOKEN);
		}

		try {
			return objectMapper.readValue(userJson, SignupRequest.class);
		} catch (final JsonProcessingException e) {
			log.error("Deserialization Error: ", e);
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
