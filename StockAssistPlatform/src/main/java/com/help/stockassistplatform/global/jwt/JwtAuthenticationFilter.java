package com.help.stockassistplatform.global.jwt;

import java.io.IOException;

import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.help.stockassistplatform.domain.user.entity.User;
import com.help.stockassistplatform.domain.user.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final String NO_CHECK_URL = "/api/auth/login";

	private final JwtUtil jwtUtil;
	private final UserRepository userRepository;

	@Override
	protected void doFilterInternal(
		@NonNull final HttpServletRequest request,
		@NonNull final HttpServletResponse response,
		@NonNull final FilterChain filterChain) throws ServletException, IOException {
		// "/api/login" 요청은 토큰 확인 x
		if (request.getRequestURI().equals(NO_CHECK_URL)) {
			filterChain.doFilter(request, response);
			return;
		}
		checkAccessTokenAndAuthentication(request, response, filterChain);
	}

	private void checkAccessTokenAndAuthentication(final HttpServletRequest request, final HttpServletResponse response,
		final FilterChain filterChain) throws ServletException, IOException {
		jwtUtil.extractAccessTokenFromRequest(request)
			.filter(jwtUtil::isTokenValidate)
			.flatMap(jwtUtil::extractUsername)
			.flatMap(userRepository::findByUsername)
			.ifPresent(this::saveAuthentication);

		filterChain.doFilter(request, response);
	}

	private void saveAuthentication(final User myUser) {
		final UserDetails userDetails = CustomUser.from(myUser);
		final Authentication authentication =
			new UsernamePasswordAuthenticationToken(
				userDetails,
				userDetails.getPassword(),
				userDetails.getAuthorities()
			);

		SecurityContextHolder.getContext().setAuthentication(authentication);
		log.info("Security Context에 '{}' 인증 정보를 저장했습니다.", myUser.getUsername());
	}
}
