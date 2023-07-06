package com.moondahoon.mobilityclient.command;

import com.moondahoon.mobilityclient.command.sub.GetCommand;
import com.moondahoon.mobilityclient.command.sub.PutCommand;
import java.util.concurrent.Callable;
import picocli.CommandLine;

@CommandLine.Command(
    name = "call",
    description = "CLI for gRPC Client Test",
    mixinStandardHelpOptions = true,
    requiredOptionMarker = '*',
    optionListHeading = "%nOptions are:%n",
    subcommands = {
        GetCommand.class,
        PutCommand.class
    }
)
public class Command implements Callable<Integer> {
//todo : success, failure 등 picoCLI 공부 추가

    final Integer SUCCESS = 0;
    final Integer FAILURE = 1;

    @Override
    public Integer call() throws Exception {
        return SUCCESS;
    }
}
