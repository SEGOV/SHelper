package com.server.service.function;

import com.client.view.SessionFunctionController;
import javafx.scene.control.CheckBox;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionService {
    private static final FunctionService INSTANCE = new FunctionService();

    public static FunctionService getInstance() {
        return INSTANCE;
    }

    public void executeFunctions(List<CheckBox> functionsList, SessionFunctionController sessionFunctionController) {
        FunctionFactory functionFactory = FunctionFactory.getInstance();
        List<CheckBox> activeCheckBoxFunctionsList = functionsList.stream().filter(checkBoxFunction -> checkBoxFunction.isSelected()).collect(Collectors.toList());
        activeCheckBoxFunctionsList.forEach((function) -> functionFactory.getFunction(function.getText()).execute(sessionFunctionController));
    }
}
