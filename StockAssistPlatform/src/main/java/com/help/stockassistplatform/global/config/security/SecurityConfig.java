package com.help.stockassistplatform.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.ExceptionTranslationFilter;

import com.help.stockassistplatform.global.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;

	// TODO: CSRF 토큰 사용 시 주석 해제
	// @Bean
	// public CsrfTokenRepository csrfTokenRepository() {
	// 	HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
	// 	repository.setHeaderName("X-XSRF-TOKEN");
	// 	return repository;
	// }

	@Bean
	public SecurityFilterChain filterChain(final HttpSecurity http) throws Exception {
		// http.csrf(csrf -> csrf.csrfTokenRepository(csrfTokenRepository())
		// 	.ignoringRequestMatchers("/login")
		// )
		http.csrf(AbstractHttpConfigurer::disable);
		http.sessionManagement(session -> session
			.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		);
		http.formLogin(AbstractHttpConfigurer::disable);
		http.authorizeHttpRequests((authorize) ->
			authorize.requestMatchers("/**").permitAll()
				.anyRequest().authenticated()
		);
		http.addFilterBefore(jwtAuthenticationFilter, ExceptionTranslationFilter.class);
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	// @Bean
	// CorsConfigurationSource corsConfigurationSource() {
	// 	CorsConfiguration configuration = new CorsConfiguration();
	//
	// 	configuration.setAllowedOrigins(List.of("http://localhost:8005"));
	// 	configuration.setAllowedMethods(List.of("GET","POST"));
	// 	configuration.setAllowedHeaders(List.of("Authorization","Content-Type"));
	//
	// 	UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
	//
	// 	source.registerCorsConfiguration("/**",configuration);
	//
	// 	return source;
	// }
}
