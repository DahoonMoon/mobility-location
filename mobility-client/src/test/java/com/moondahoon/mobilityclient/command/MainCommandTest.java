package com.moondahoon.mobilityclient.command;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.PrintWriter;
import java.io.StringWriter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import picocli.CommandLine;

class MainCommandTest {
	private MainCommand mainCommand;
	private StringWriter out;
	private PrintWriter printWriter;

	@BeforeEach
	void setUp() {
		mainCommand = new MainCommand();
		out = new StringWriter();
		printWriter = new PrintWriter(out, true);
	}

	@DisplayName("Main Command 테스트")
	@Test
	void callTest() {
		// Given
		CommandLine commandLine = new CommandLine(mainCommand);
		commandLine.setOut(printWriter);

		// When
		int result = commandLine.execute();

		// Then
		assertThat(result).isEqualTo(mainCommand.SUCCESS);
	}
}