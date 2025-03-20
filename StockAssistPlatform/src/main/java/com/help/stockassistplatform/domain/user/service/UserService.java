package com.help.stockassistplatform.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.common.exception.CustomException;
import com.help.stockassistplatform.common.exception.ErrorCode;
import com.help.stockassistplatform.domain.user.dto.SignupRequest;
import com.help.stockassistplatform.domain.user.entity.User;
import com.help.stockassistplatform.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void registerUser(final SignupRequest userInfo) {
		final String encodedPassword = passwordEncoder.encode(userInfo.getPassword());
		final User user = User.builder()
			.email(userInfo.getEmail())
			.password(encodedPassword)
			.nickname(userInfo.getNickname())
			.build();
		userRepository.save(user);
	}

	public void validateDuplicateEmail(final String email) {
		if (userRepository.existsByEmail(email)) {
			throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}
	}
}
