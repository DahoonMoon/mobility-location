package com.moondahoon.mobilityserver.exception;

import io.grpc.Status;
import net.devh.boot.grpc.server.advice.GrpcAdvice;
import net.devh.boot.grpc.server.advice.GrpcExceptionHandler;

@GrpcAdvice
public class GrpcExceptionAdvice {

	@GrpcExceptionHandler
	public Status handleInvalidArgument(IllegalArgumentException e) {
		return Status.INVALID_ARGUMENT.withDescription("Invalid Args").withCause(e);
	}

//	@GrpcExceptionHandler(ResourceNotFoundException.class)
//	public StatusException handleResourceNotFoundException(ResourceNotFoundException e) {
//		Status status = Status.NOT_FOUND.withDescription("Your description").withCause(e);
//		Metadata metadata = ...
//		return status.asException(metadata);
//	}

}
