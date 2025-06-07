package com.help.stockassistplatform.domain.wishlist.controller;

import java.util.List;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.help.stockassistplatform.domain.wishlist.dto.WishlistItemDto;
import com.help.stockassistplatform.domain.wishlist.dto.WishlistRequestDto;
import com.help.stockassistplatform.domain.wishlist.service.WishlistService;
import com.help.stockassistplatform.global.common.response.ApiResponse;
import com.help.stockassistplatform.global.jwt.CustomUser;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@PreAuthorize("isAuthenticated()")
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
public class WishlistController {

	private final WishlistService wishlistService;

	@PostMapping
	public ApiResponse<Void> add(
		@AuthenticationPrincipal CustomUser currentUser,
		@Valid @RequestBody WishlistRequestDto request
	) {
		wishlistService.add(currentUser, request.ticker());
		return ApiResponse.success(null);
	}

	@DeleteMapping("/{ticker}")
	public ApiResponse<Void> remove(
		@AuthenticationPrincipal CustomUser currentUser,
		@PathVariable String ticker
	) {
		wishlistService.remove(currentUser, ticker);
		return ApiResponse.success(null);
	}

	@GetMapping
	public ApiResponse<List<WishlistItemDto>> list(
		@AuthenticationPrincipal CustomUser currentUser
	) {
		return ApiResponse.success(wishlistService.list(currentUser));
	}
}
