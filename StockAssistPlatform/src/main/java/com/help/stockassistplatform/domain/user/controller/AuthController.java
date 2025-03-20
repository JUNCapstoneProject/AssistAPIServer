package com.help.stockassistplatform.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.common.response.ApiResponse;
import com.help.stockassistplatform.domain.user.dto.SignupRequest;
import com.help.stockassistplatform.domain.user.service.AuthTokenService;
import com.help.stockassistplatform.domain.user.service.EmailService;
import com.help.stockassistplatform.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserService userService;
	private final AuthTokenService authTokenService;
	private final EmailService emailService;

	// 회원가입 요청
	@PostMapping("/register")
	public ApiResponse<?> signup(@Valid @RequestBody final SignupRequest request) {
		userService.validateDuplicateEmail(request.getEmail());
		final String token = authTokenService.createToken();
		authTokenService.saveUserInfoToRedis(token, request);
		emailService.sendVerificationEmail(token, request.getEmail());
		return ApiResponse.success(null);
	}

	// 프론트에서 보낸 토큰 정보를 바탕으로 유저 회원가입 처리
	@GetMapping("/verify")
	public ApiResponse<?> verify(@RequestParam final String token) {
		final SignupRequest userInfo = authTokenService.getUserInfoFromRedis(token);
		userService.registerUser(userInfo);
		return ApiResponse.success(null);
	}
}
