package com.help.stockassistplatform.domain.news.dto;

public record CategoryStatusDto(
	String name,
	String status,
	Integer aiScore
) {
}
