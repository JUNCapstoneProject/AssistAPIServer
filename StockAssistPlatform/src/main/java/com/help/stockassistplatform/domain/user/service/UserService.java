package com.help.stockassistplatform.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.help.stockassistplatform.domain.user.dto.SignupRequest;
import com.help.stockassistplatform.domain.user.entity.User;
import com.help.stockassistplatform.domain.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class UserService {
	private final UserRepository userRepository;

	@Transactional
	public void registerUser(final SignupRequest request) {
		final User user = User.builder()
			.email(request.getEmail())
			.password(request.getPassword())
			.nickname(request.getNickname())
			.build();
		userRepository.save(user);
	}
}
