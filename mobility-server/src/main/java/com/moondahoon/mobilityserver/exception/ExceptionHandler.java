package com.moondahoon.mobilityserver.exception;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class ExceptionHandler {

	@GrpcExceptionHandler
	public Status handleInvalidArgument(IllegalArgumentException e) {
		return Status.INVALID_ARGUMENT.withDescription(e.getMessage()).withCause(e);
	}

}
