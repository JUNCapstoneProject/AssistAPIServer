package com.help.stockassistplatform.global.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.help.stockassistplatform.domain.user.repository.UserRepository;
import com.help.stockassistplatform.global.common.response.ApiResponse;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
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
		final String refreshToken = jwtUtil.createRefreshToken();
		response.addHeader("Set-Cookie", jwtUtil.createRefreshTokenCookie(refreshToken).toString());
		response.addHeader("Authorization", accessToken);

		return ApiResponse.success(null);
	}
}
