package com.help.stockassistplatform.domain.user.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.user.dto.request.PasswordChangeRequestDto;
import com.help.stockassistplatform.domain.user.dto.request.ProfileUpdateRequestDto;
import com.help.stockassistplatform.domain.user.dto.request.SignupRequestDto;
import com.help.stockassistplatform.domain.user.entity.User;
import com.help.stockassistplatform.domain.user.repository.UserRepository;
import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;
import com.help.stockassistplatform.global.jwt.CustomUser;

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
		userRepository.save(user);
		log.info("User registered: {}", user);
	}

	@Transactional
	public void updateUserPassword(
		final CustomUser user,
		final PasswordChangeRequestDto requestDto
	) {
		final User loginUser = userRepository.findByUsername(user.getUsername())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
		if (!passwordEncoder.matches(requestDto.oldPassword(), loginUser.getPassword())) {
			throw new CustomException(ErrorCode.INVALID_PASSWORD);
		}
		loginUser.updatePassword(passwordEncoder.encode(requestDto.newPassword()));
		userRepository.save(loginUser);
		log.info("User password updated: {}", loginUser);
	}

	@Transactional
	public void updateUserProfile(final User loginUser, final ProfileUpdateRequestDto requestDto) {
		loginUser.getUserProfile().updateNickname(requestDto.getNickname());
		requestDto.getDescription().ifPresent(loginUser.getUserProfile()::updateDescription);
		userRepository.save(loginUser);
		log.info("User profile updated: {}", loginUser);
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
