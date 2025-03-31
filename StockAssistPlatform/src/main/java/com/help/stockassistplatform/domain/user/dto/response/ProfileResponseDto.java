package com.help.stockassistplatform.domain.user.dto.response;

import com.help.stockassistplatform.domain.user.entity.UserProfile;

public record ProfileResponseDto(UserInfo user) {
	public static ProfileResponseDto from(final UserProfile userProfile) {
		return new ProfileResponseDto(new UserInfo(userProfile.getNickname(), userProfile.getEmail()));
	}

	private record UserInfo(String nickname, String email) {
	}
}
