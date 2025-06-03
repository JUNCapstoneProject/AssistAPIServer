package com.help.stockassistplatform.domain.stock.dto.response;

import java.util.List;

public record SliceResponse<T>(
	List<T> data,
	int total,
	boolean hasNext
) {
}
