package com.help.stockassistplatform.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.domain.user.dto.SignupRequestDto;
import com.help.stockassistplatform.domain.user.service.EmailVerificationService;
import com.help.stockassistplatform.domain.user.service.UserService;
import com.help.stockassistplatform.domain.user.service.VerificationTokenService;
import com.help.stockassistplatform.global.common.response.ApiResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class RegisterController {
	private final UserService userService;
	private final VerificationTokenService verificationTokenService;
	private final EmailVerificationService emailVerificationService;

	// 회원가입 요청
	@PostMapping("/register")
	public ApiResponse<?> signup(@Valid @RequestBody final SignupRequestDto request) {
		userService.validateDuplicateEmail(request.getEmail());
		final String token = verificationTokenService.createToken();
		verificationTokenService.saveUserInfoToRedis(token, request);
		emailVerificationService.sendVerificationEmail(token, request.getEmail());
		return ApiResponse.success(null);
	}

	// 토큰 정보를 바탕으로 유저 회원가입 처리
	@GetMapping("/verify")
	public ApiResponse<?> verify(@RequestParam final String token) {
		final SignupRequestDto userInfo = verificationTokenService.getUserInfoFromRedis(token);
		userService.registerUser(userInfo);
		return ApiResponse.success(null);
	}
}
