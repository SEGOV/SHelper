package com.server.service.function;

import com.client.view.controller.SessionFunctionController;
import com.server.exception.ShelperException;

public interface Function {
    void execute(SessionFunctionController sessionFunctionController) throws ShelperException;
}
