package com.help.stockassistplatform.global.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.help.stockassistplatform.domain.user.entity.User;
import com.help.stockassistplatform.domain.user.repository.UserRepository;
import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;
import com.help.stockassistplatform.global.common.response.ApiResponse;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RequiredArgsConstructor
@Slf4j
@Service
public class LoginService implements UserDetailsService {
	private final UserRepository userRepository;
	private final AuthenticationManagerBuilder authenticationManagerBuilder;
	private final JwtUtil jwtUtil;

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		final var user = userRepository.findByUsername(username)
			.orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다"));

		final CustomUser userDetails = CustomUser.from(user);
		userDetails.setUserId(user.getUserId());
		return userDetails;
	}

	public ApiResponse<?> login(final LoginUserDto loginUserDto, final HttpServletResponse response) {
		final Authentication authentication = authenticationManagerBuilder.getObject()
			.authenticate(new UsernamePasswordAuthenticationToken(loginUserDto.getEmail(), loginUserDto.getPassword()));
		final CustomUser user = (CustomUser)authentication.getPrincipal();
		final String accessToken = jwtUtil.createAccessToken(user);
		final String refreshToken = jwtUtil.createRefreshToken(user);
		response.addHeader("Set-Cookie", jwtUtil.createRefreshTokenCookie(refreshToken).toString());
		response.addHeader("Authorization", accessToken);

		return ApiResponse.success(null);
	}

	public ApiResponse<?> refresh(final HttpServletRequest request, final HttpServletResponse response) {
		final User user = jwtUtil.extractRefreshTokenFromRequest(request)
			.filter(jwtUtil::isTokenValidate)
			.flatMap(jwtUtil::extractUsername)
			.flatMap(userRepository::findByUsername)
			.orElseThrow(() -> new CustomException(ErrorCode.EXPIRED_TOKEN));

		final String accessToken = jwtUtil.createAccessToken(CustomUser.from(user));
		response.addHeader("Authorization", accessToken);
		log.info("AccessToken 재발급: {}", accessToken);
		return ApiResponse.success(null);
	}

	// logout
	public ApiResponse<?> logout(final HttpServletResponse response) {
		response.addHeader("Set-Cookie", jwtUtil.createExpiredRefreshTokenCookie().toString());
		return ApiResponse.success(null);
	}
}
