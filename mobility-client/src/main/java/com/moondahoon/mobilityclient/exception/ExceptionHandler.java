package com.moondahoon.mobilityclient.exception;

import picocli.CommandLine;
import picocli.CommandLine.IExecutionExceptionHandler;
import picocli.CommandLine.ParseResult;

public class ExceptionHandler implements IExecutionExceptionHandler {

	@Override
	public int handleExecutionException(Exception ex, CommandLine commandLine, ParseResult parseResult) throws Exception {
		System.out.println(CommandLine.Help.Ansi.AUTO.string(String.format("@|bold,red ERROR : %s |@", ex.getMessage())));
		return commandLine.getExitCodeExceptionMapper() != null
				? commandLine.getExitCodeExceptionMapper().getExitCode(ex)
				: commandLine.getCommandSpec().exitCodeOnExecutionException();
	}
}
