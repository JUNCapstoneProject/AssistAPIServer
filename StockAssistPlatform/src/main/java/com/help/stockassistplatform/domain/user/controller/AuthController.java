package com.help.stockassistplatform.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.domain.user.dto.request.LoginRequestDto;
import com.help.stockassistplatform.domain.user.dto.response.LoginCheckResponseDto;
import com.help.stockassistplatform.domain.user.service.AuthService;
import com.help.stockassistplatform.global.common.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;

	// 로그인
	@PostMapping("/login")
	public ApiResponse<?> login(@Valid @RequestBody final LoginRequestDto loginRequestDto, final
	HttpServletResponse response) {
		return authService.login(loginRequestDto, response);
	}

	// AccessToken 갱신 요청
	@PostMapping("/refresh")
	public ApiResponse<?> refresh(final HttpServletRequest request, final HttpServletResponse response) {
		return authService.refresh(request, response);
	}

	// 로그아웃
	@PostMapping("/logout")
	public ApiResponse<?> logout(final HttpServletResponse response) {
		authService.logout(response);
		return ApiResponse.success(null);
	}

	@GetMapping("/check")
	public ApiResponse<?> loginCheck(final HttpServletRequest request) {
		final Boolean isLogin = authService.checkLogin(request);
		return ApiResponse.success(new LoginCheckResponseDto(isLogin));
	}
}
