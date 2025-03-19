package com.help.stockassistplatform.domain.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.common.response.ApiResponse;
import com.help.stockassistplatform.domain.user.dto.SignupRequest;
import com.help.stockassistplatform.domain.user.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
	private final UserService userService;

	// 회원가입 요청
	@PostMapping("/register")
	public ApiResponse<?> signup(@Valid @RequestBody final SignupRequest request) {
		userService.validateDuplicateEmail(request.getEmail());
		userService.registerUser(request);
		return ApiResponse.success(null);
	}
}
