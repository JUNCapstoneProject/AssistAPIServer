package com.help.stockassistplatform.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class SignupRequest {
	@NotBlank
	private String email;

	@NotBlank
	private String password;

	@NotBlank
	private String nickname;
}
