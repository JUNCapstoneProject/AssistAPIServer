package com.help.stockassistplatform.global.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Getter;
import lombok.Setter;

public class CustomUser extends User {
	@Getter
	@Setter
	private UUID userId;
	@Getter
	@Setter
	private String nickname;

	public CustomUser(
		String username,
		String password,
		Collection<? extends GrantedAuthority> authorities
	) {
		super(username, password, authorities);
	}

	public static CustomUser from(final com.help.stockassistplatform.domain.user.entity.User user) {
		final List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(user.getRole().getRoleName()));
		final CustomUser customUser = new CustomUser(
			user.getUsername(),
			user.getPassword(),
			authorities
		);
		customUser.setUserId(user.getUserId());
		customUser.setNickname(user.getUserProfile().getNickname());

		return customUser;
	}
}
