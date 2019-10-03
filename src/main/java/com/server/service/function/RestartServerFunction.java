package com.server.service.function;

import com.client.view.controller.SessionFunctionController;

public class RestartServerFunction implements Function {
    @Override
    public void execute(SessionFunctionController sessionFunctionController) {
        System.out.println("restart server");
    }
}
