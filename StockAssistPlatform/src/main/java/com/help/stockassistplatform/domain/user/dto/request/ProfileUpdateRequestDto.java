package com.help.stockassistplatform.domain.user.dto.request;

import java.util.Optional;

import lombok.Data;

@Data
public class ProfileUpdateRequestDto {
	private String nickname;
	private Optional<String> description = Optional.empty();
}
