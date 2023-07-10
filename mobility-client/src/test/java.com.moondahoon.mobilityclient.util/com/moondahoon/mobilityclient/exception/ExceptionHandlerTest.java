package com.moondahoon.mobilityclient.exception;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import picocli.CommandLine;
import picocli.CommandLine.ParseResult;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerTest {

	@Mock
	private CommandLine commandLine;
	@Mock
	private ParseResult parseResult;
	@Mock
	private CommandLine.IExitCodeExceptionMapper exitCodeExceptionMapper;

	private ExceptionHandler exceptionHandler;
	private Exception exception;

	@BeforeEach
	public void setup() {
		exceptionHandler = new ExceptionHandler();
		exception = new Exception("Test exception");
	}

	@DisplayName("Client Exception Handler 테스트")
	@Test
	public void handleExecutionExceptionTest() throws Exception {
//		given & when
		Mockito.when(commandLine.getExitCodeExceptionMapper()).thenReturn(exitCodeExceptionMapper);
		Mockito.when(exitCodeExceptionMapper.getExitCode(exception)).thenReturn(1);

//		then
		int exitCode = exceptionHandler.handleExecutionException(exception, commandLine, parseResult);
		assertThat(exitCode).isEqualTo(1);
	}

}