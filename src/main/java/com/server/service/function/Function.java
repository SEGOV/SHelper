package com.server.service.function;

import com.client.view.SessionFunctionController;
import com.server.exception.ShelperException;

public interface Function {
    void execute(SessionFunctionController sessionFunctionController) throws ShelperException;
}
