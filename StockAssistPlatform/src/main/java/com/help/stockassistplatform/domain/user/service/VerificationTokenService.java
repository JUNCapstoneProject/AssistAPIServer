package com.help.stockassistplatform.domain.user.service;

import java.time.Duration;
import java.util.UUID;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.help.stockassistplatform.domain.user.dto.request.SignupRequestDto;
import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class VerificationTokenService {
	private static final long EXPIRE_MINUTES = 10L;
	private static final String TOKEN_PREFIX = "auth:email:";
	private final RedisTemplate<String, Object> redisTemplate;
	private final ObjectMapper objectMapper;

	public String createToken() {
		return UUID.randomUUID().toString();
	}

	public void saveUserInfoToRedis(final String token, final SignupRequestDto request) {
		try {
			final String userJson = objectMapper.writeValueAsString(request);
			redisTemplate.opsForValue().set(TOKEN_PREFIX + token, userJson, Duration.ofMinutes(EXPIRE_MINUTES));
			log.info("Token: {}, UserRequest: {}", token, userJson);
		} catch (final JsonProcessingException e) {
			log.error("Serialization Error: ", e);
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	public SignupRequestDto getUserInfoFromRedis(final String token) {
		final String userJson = (String)redisTemplate.opsForValue().get(TOKEN_PREFIX + token);
		if (userJson == null) {
			throw new CustomException(ErrorCode.EXPIRED_TOKEN);
		}

		try {
			return objectMapper.readValue(userJson, SignupRequestDto.class);
		} catch (final JsonProcessingException e) {
			log.error("Deserialization Error: ", e);
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
}
