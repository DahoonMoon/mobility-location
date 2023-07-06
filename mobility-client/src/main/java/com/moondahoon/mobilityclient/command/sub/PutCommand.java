package com.moondahoon.mobilityclient.command.sub;

import picocli.CommandLine;
import picocli.CommandLine.Command;

@CommandLine.Command(
	name = "put",
	description = "put vehicle location",
	mixinStandardHelpOptions = true,
	requiredOptionMarker = '*',
	optionListHeading = "%nOptions are:%n"
)
public class PutCommand implements Runnable{

	@CommandLine.Option(
		names = {"-m", "--id"},
		required = true,
		description = "vehicle id"
	)
	private String id;

	final Integer SUCCESS = 0;
	final Integer FAILURE = 1;

	@Override
	public void run() {
		System.out.println("Hello world");
	}
}
