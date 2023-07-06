package com.moondahoon.mobilityclient;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Parameters;

//@SpringBootApplication
@Command(
		name = "helloname",
		description = "hello"
)
public class GetCommand implements Runnable{

	@Parameters(index = "0", description = "hey")
	private String name;

	public static void main(String[] args) {

		new CommandLine(new GetCommand()).execute(args);
	}

	@Override
	public void run() {
		System.out.println("Hello world " + name);

	}
}
