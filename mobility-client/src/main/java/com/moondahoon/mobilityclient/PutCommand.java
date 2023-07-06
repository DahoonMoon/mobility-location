package com.moondahoon.mobilityclient;

import picocli.CommandLine;
import picocli.CommandLine.Command;

//@SpringBootApplication
@Command(
		name = "helloworld",
		description = "hello"
)
public class PutCommand implements Runnable{

	public static void main(String[] args) {
		new CommandLine(new PutCommand()).execute(args);
	}

	@Override
	public void run() {
		System.out.println("Hello world");
	}
}
