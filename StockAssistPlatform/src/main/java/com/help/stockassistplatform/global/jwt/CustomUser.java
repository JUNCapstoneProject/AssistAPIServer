package com.help.stockassistplatform.global.jwt;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import lombok.Setter;

public class CustomUser extends User {
	@Setter
	private Long userId;

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
		return new CustomUser(
			user.getUsername(),
			user.getPassword(),
			authorities
		);
	}
}
