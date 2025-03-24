package com.help.stockassistplatform.global.config.security;

import java.util.List;

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
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.help.stockassistplatform.global.jwt.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final EmailVerificationFilter emailVerificationFilter;

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
		http.cors(corsCustomizer -> corsCustomizer.configurationSource(configurationSource()));
		http.formLogin(AbstractHttpConfigurer::disable);
		http.authorizeHttpRequests((authorize) ->
			authorize.requestMatchers("/**").permitAll()
				.anyRequest().authenticated()
		);
		http.addFilterBefore(jwtAuthenticationFilter, ExceptionTranslationFilter.class)
			.addFilterBefore(emailVerificationFilter, JwtAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}

	@Bean
	public CorsConfigurationSource configurationSource() {
		final CorsConfiguration configuration = new CorsConfiguration();

		// 허용할 Origin 지정 TODO: 프론트엔드 출처로 변경
		configuration.setAllowedOrigins(List.of(
			"*"
		));

		configuration.setAllowedMethods(List.of("*"));
		configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));
		configuration.setExposedHeaders(List.of("Authorization"));
		configuration.setAllowCredentials(true);

		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);

		return source;
	}
}
