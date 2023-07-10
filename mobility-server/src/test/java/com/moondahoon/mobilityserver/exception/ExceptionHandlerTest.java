package com.moondahoon.mobilityserver.exception;

import static org.assertj.core.api.Assertions.assertThat;

import io.grpc.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ExceptionHandlerTest {

	@DisplayName("Exception 발생시 Handler 작동 테스트")
	@Test
	void testHandleInvalidArgument() {
		// given
		String message = "error message";
		IllegalArgumentException exception = new IllegalArgumentException(message);

		// when
		ExceptionHandler handler = new ExceptionHandler();
		Status status = handler.handleInvalidArgument(exception);

		// then
		assertThat(status.getCode()).isEqualTo(Status.INVALID_ARGUMENT.getCode());
		assertThat(status.getDescription()).isEqualTo(message);
		assertThat(status.getCause()).isEqualTo(exception);
	}

}