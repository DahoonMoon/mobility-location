package com.moondahoon.mobilityclient;

import com.moondahoon.mobilityclient.command.MainCommand;
import com.moondahoon.mobilityclient.exception.ExceptionHandler;
import picocli.CommandLine;

public class Application {

    public static void main(String[] args) {
        new CommandLine(new MainCommand())
                .setExecutionExceptionHandler(new ExceptionHandler())
                .setUsageHelpWidth(100)
                .execute(args);
    }

}
