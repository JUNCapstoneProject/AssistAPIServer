package com.help.stockassistplatform.domain.wishlist.exception;

import com.help.stockassistplatform.global.common.exception.CustomException;
import com.help.stockassistplatform.global.common.exception.ErrorCode;

public class StockNotFoundException extends CustomException {
	public StockNotFoundException(String ticker) {
		super(ErrorCode.HEART_TICKER_NOT_FOUND);
	}
}
