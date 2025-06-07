package com.help.stockassistplatform.global.jwt;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.help.stockassistplatform.domain.user.entity.User;

import lombok.Getter;

@Getter
public class CustomUser extends org.springframework.security.core.userdetails.User {
	private final User domainUser;
	private final UUID userId;
	private final String nickname;

	private CustomUser(
		String username,
		String password,
		Collection<? extends GrantedAuthority> authorities,
		User domainUser
	) {
		super(username, password, authorities);
		this.domainUser = domainUser;
		this.userId = domainUser.getUserId();
		this.nickname = domainUser.getUserProfile().getNickname();
	}

	public static CustomUser from(final User user) {
		final List<GrantedAuthority> auth =
			List.of(new SimpleGrantedAuthority(user.getRole().getRoleName()));
		return new CustomUser(user.getUsername(), user.getPassword(), auth, user);
	}
}
