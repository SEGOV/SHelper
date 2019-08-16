package com.server.service.function;

import com.client.view.SessionFunctionController;
import com.server.model.ssh.SSHCleanBoiler;

public class CleanBoilerFunction implements Function {
    @Override
    public void execute(SessionFunctionController sessionFunctionController) {
        System.out.println("clean boiler");
        new SSHCleanBoiler().executeCleanCommand(sessionFunctionController);
    }
}
