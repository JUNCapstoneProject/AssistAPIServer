package com.help.stockassistplatform.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.domain.user.dto.SignupRequest;
import com.help.stockassistplatform.domain.user.service.AuthTokenService;
import com.help.stockassistplatform.domain.user.service.EmailService;
import com.help.stockassistplatform.domain.user.service.UserService;
import com.help.stockassistplatform.global.common.response.ApiResponse;
import com.help.stockassistplatform.global.jwt.LoginService;
import com.help.stockassistplatform.global.jwt.LoginUserDto;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserService userService;
	private final AuthTokenService authTokenService;
	private final EmailService emailService;
	private final LoginService loginService;

	// 회원가입 요청
	@PostMapping("/register")
	public ApiResponse<?> signup(@Valid @RequestBody final SignupRequest request) {
		userService.validateDuplicateEmail(request.getEmail());
		final String token = authTokenService.createToken();
		authTokenService.saveUserInfoToRedis(token, request);
		emailService.sendVerificationEmail(token, request.getEmail());
		return ApiResponse.success(null);
	}

	// 토큰 정보를 바탕으로 유저 회원가입 처리
	@GetMapping("/verify")
	public ApiResponse<?> verify(@RequestParam final String token) {
		final SignupRequest userInfo = authTokenService.getUserInfoFromRedis(token);
		userService.registerUser(userInfo);
		return ApiResponse.success(null);
	}

	// 로그인 TODO: loginSuccessHandler migration
	@PostMapping("/login")
	public ApiResponse<?> login(@RequestBody final LoginUserDto loginUserDto, final
	HttpServletResponse response) {
		return loginService.login(loginUserDto, response);
	}

	@PostMapping("/refresh")
	public ApiResponse<?> refresh(HttpServletRequest request, HttpServletResponse response) {
		return loginService.refresh(request, response);
	}

	// 로그아웃
	@PostMapping("/logout")
	public ApiResponse<?> logout(final HttpServletResponse response) {
		loginService.logout(response);
		return ApiResponse.success(null);
	}

	// 테스트용
	// @GetMapping("/me")
	// public ApiResponse<?> getMyProfile(@AuthenticationPrincipal CustomUser userDetails,
	// 	final HttpServletResponse request) {
	// 	if (userDetails == null) {
	// 		return ApiResponse.error(ErrorCode.UNAUTHORIZED);
	// 	}
	//
	// 	return ApiResponse.success(userDetails);
	// }
}
