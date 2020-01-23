package com.server.service.function;

import com.client.view.controller.SessionFunctionController;
import com.server.Constants;

import java.util.ArrayList;
import java.util.List;

import static com.server.Constants.Server.SERVER_PATH;
import static com.server.Constants.Server.SERVER_STOP_ALL_NODES_COMMAND_NAME;

public class RestartServerFunction implements Function {
    @Override
    public void execute(SessionFunctionController sessionFunctionController) {
        List<String> commands = new ArrayList<>();
        commands.add("cd " + SERVER_PATH);
        commands.add(Constants.Server.SERVER_STOP_ALL_NODES_COMMAND);

        ScriptShExecutor scriptShExecutor = new ScriptShExecutor(sessionFunctionController);
        scriptShExecutor.executeCommands(SERVER_STOP_ALL_NODES_COMMAND_NAME, commands);
    }
}
