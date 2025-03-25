package com.help.stockassistplatform.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.user.dto.SignupRequestDto;
import com.help.stockassistplatform.domain.user.entity.User;
import com.help.stockassistplatform.domain.user.repository.UserRepository;
import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Transactional
	public void registerUser(final SignupRequestDto userInfo) {
		final String encodedPassword = passwordEncoder.encode(userInfo.getPassword());
		validateDuplicateEmail(userInfo.getEmail());
		final User user = User.builder()
			.username(userInfo.getEmail())
			.password(encodedPassword)
			.nickname(userInfo.getNickname())
			.build();
		log.info("User registered: {}", user);
		userRepository.save(user);
	}

	@Transactional
	public void updateUserPassword(final User user) {
		userRepository.save(user);
	}

	public User findUserByUsername(final String username) {
		return userRepository.findWithProfileByUsername(username)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}

	public void validateDuplicateEmail(final String email) {
		if (userRepository.existsByUsername(email)) {
			throw new CustomException(ErrorCode.EMAIL_ALREADY_EXISTS);
		}
	}
}
