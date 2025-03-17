package com.help.stockassistplatform.common.exception;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.help.stockassistplatform.common.response.ApiResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ApiResponse<?>> handleValidationException(final MethodArgumentNotValidException ex) {
		log.error("MethodArgumentNotValidException : {}", ex.getMessage());
		final BindingResult bindingResult = ex.getBindingResult();
		final List<FieldError> fieldErrors = bindingResult.getFieldErrors();

		final String errorMessage = fieldErrors.stream()
			.findFirst()
			.map(FieldError::getDefaultMessage)
			.orElse("Validation error");

		return ResponseEntity
			.status(HttpStatus.BAD_REQUEST)
			.body(ApiResponse.error(errorMessage, HttpStatus.BAD_REQUEST));
	}

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ApiResponse<?>> handleCustomException(final CustomException ex) {
		log.error("CustomException : {}", ex.getMessage());
		final ErrorCode errorCode = ex.getErrorCode();
		return ResponseEntity
			.status(errorCode.getStatus())
			.body(ApiResponse.error(errorCode.getMessage(), HttpStatusCode.valueOf(errorCode.getStatus())));
	}

	@ExceptionHandler(RuntimeException.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public ApiResponse<?> unknownServerError(final RuntimeException e) {
		log.error("서버 오류 : {}", e.getMessage());
		return ApiResponse.error("e.getMessage()", HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
