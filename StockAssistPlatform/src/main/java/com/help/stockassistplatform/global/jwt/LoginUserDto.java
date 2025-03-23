package com.help.stockassistplatform.global.jwt;

import lombok.Data;

@Data
public class LoginUserDto {
	private String email;
	private String password;
}
