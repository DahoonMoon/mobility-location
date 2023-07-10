package com.moondahoon.mobilityclient.command;

import com.moondahoon.mobilityclient.command.sub.GetCommand;
import com.moondahoon.mobilityclient.command.sub.HistoryCommand;
import com.moondahoon.mobilityclient.command.sub.PutCommand;
import com.moondahoon.mobilityclient.command.sub.SearchCommand;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
		name = "command",
		description = "CLI for Testing gRPC Client about Vehicle Location",
		mixinStandardHelpOptions = true,
		requiredOptionMarker = '*',
		optionListHeading = "%nOptions are:%n",
		subcommands = {
				PutCommand.class,
				GetCommand.class,
				SearchCommand.class,
				HistoryCommand.class
		}
)
public class MainCommand implements Callable<Integer> {

	final Integer SUCCESS = 0;
	final Integer FAILURE = 1;

	@Override
	public Integer call() throws Exception {
		CommandLine.usage(this, System.out);
		return SUCCESS;
	}
}
