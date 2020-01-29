package com.server.service.function;

import com.client.view.controller.SessionFunctionController;
import com.server.model.ssh.SSHManager;

import static com.server.Constants.Boiler.*;

public class CleanBoilerFunction implements Function {

    @Override
    public void execute(SessionFunctionController functionController) {
        SSHManager.getInstance().fetchSSHManager(functionController.getSession());
        ScriptShExecutor scriptShExecutor = new ScriptShExecutor(functionController);

        scriptShExecutor.uploadScriptIfNotExist(CLEAN_BOILER_SCRIPT_NAME, CLEAN_BOILER_SCRIPT_MISSED_MESSAGE);
        scriptShExecutor.executeCommands(CLEAN_BOILER_SCRIPT_NAME);
    }
}
