package com.help.stockassistplatform.domain.report.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserReportRequest(
	@NotBlank(message = "제목을 작성해주세요")
	@Size(max = 100, message = "제목은 100자 이하여야 합니다")
	String title,

	@NotBlank(message = "내용을 작성해주세요")
	String content,

	@NotBlank(message = "카테고리를 선택해주세요")
	String category
) {
}
