package com.server.service.function;

import com.client.view.controller.SessionFunctionController;
import com.server.model.ssh.SSHManager;

import static com.server.Constants.Server.*;

public class RestartServerFunction implements Function {
    @Override
    public void execute(SessionFunctionController functionController) {
        SSHManager.getInstance().fetchSSHManager(functionController.getSession());
        ScriptShExecutor scriptShExecutor = new ScriptShExecutor(functionController);

        scriptShExecutor.uploadScriptIfNotExist(STOP_SERVER_NODES_SCRIPT_NAME, STOP_SERVER_NODES_SCRIPT_MISSED_MESSAGE);
        scriptShExecutor.executeCommands(STOP_SERVER_NODES_SCRIPT_NAME);
    }
}
