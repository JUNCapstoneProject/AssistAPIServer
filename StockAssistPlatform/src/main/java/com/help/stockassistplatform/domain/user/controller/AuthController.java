package com.help.stockassistplatform.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.domain.user.dto.request.LoginRequestDto;
import com.help.stockassistplatform.domain.user.dto.request.PasswordResetConfirmDto;
import com.help.stockassistplatform.domain.user.dto.request.PasswordResetRequestDto;
import com.help.stockassistplatform.domain.user.dto.response.LoginCheckResponseDto;
import com.help.stockassistplatform.domain.user.service.AuthService;
import com.help.stockassistplatform.domain.user.service.EmailVerificationService;
import com.help.stockassistplatform.domain.user.service.UserService;
import com.help.stockassistplatform.domain.user.service.VerificationTokenService;
import com.help.stockassistplatform.global.common.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/auth")
@Slf4j
@RequiredArgsConstructor
public class AuthController {
	private final AuthService authService;
	private final UserService userService;

	private final VerificationTokenService tokenService;
	private final EmailVerificationService emailService;

	// 로그인
	@PostMapping("/login")
	public ApiResponse<?> login(
		@Valid @RequestBody final LoginRequestDto loginRequestDto,
		@RequestParam(value = "redirectUrl", defaultValue = "/") final String redirectUrl,
		final HttpServletResponse response
	) {
		return authService.login(loginRequestDto, redirectUrl, response);
	}

	// AccessToken 갱신 요청
	@PostMapping("/refresh")
	public ApiResponse<?> refresh(
		final HttpServletRequest request,
		final HttpServletResponse response
	) {
		log.info("[POST] /api/auth/refresh");
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

	@PostMapping("/password-reset-request")
	public ApiResponse<?> requestPasswordReset(@Valid @RequestBody final PasswordResetRequestDto resetRequestDto) {
		final String email = resetRequestDto.email();
		log.info("Password reset requested for email: {}", email);

		final String token = tokenService.createToken();
		tokenService.saveResetEmailToRedis(token, email);
		emailService.sendPasswordResetEmail(token, email);
		return ApiResponse.success(null);
	}

	@PostMapping("/password-reset")
	public ApiResponse<?> confirmPasswordReset(
		@Valid @RequestBody final PasswordResetConfirmDto request
	) {
		final String email = tokenService.getResetEmailFromRedis(request.token());
		userService.validateEmailExistence(email);
		userService.changeUserPassword(email, request.newPassword());
		return ApiResponse.success(null);
	}
}
