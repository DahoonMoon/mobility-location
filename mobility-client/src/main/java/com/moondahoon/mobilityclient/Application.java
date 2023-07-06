package com.moondahoon.mobilityclient;

import com.moondahoon.mobilityclient.command.Command;
import picocli.CommandLine;

public class Application {

//    todo : 어플리케이션 실행단 분석
    public static void main(String[] args) {
        String[] arguments = new String[]{"get", "--id=john"};
        new CommandLine(new Command()).execute(args);
    }

}
