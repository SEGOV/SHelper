package com.server.service.function;

import com.client.view.controller.SessionFunctionController;
import com.server.exception.ShelperException;
import com.server.service.validator.ValidationService;
import javafx.scene.control.CheckBox;

import java.util.List;
import java.util.stream.Collectors;

public class FunctionService {
    private static final FunctionService INSTANCE = new FunctionService();

    public static FunctionService getInstance() {
        return INSTANCE;
    }

    public void executeFunctions(List<CheckBox> functionsList, SessionFunctionController sessionFunctionController) throws ShelperException {
        FunctionFactory functionFactory = FunctionFactory.getInstance();
        List<CheckBox> activeCheckBoxFunctionsList = functionsList.stream().filter(checkBoxFunction -> checkBoxFunction.isSelected()).collect(Collectors.toList());

        boolean isConfirmed = new ValidationService().isConfirmToExecuteFunctions(activeCheckBoxFunctionsList);
        if(isConfirmed) {
            for (CheckBox function : activeCheckBoxFunctionsList) {
                functionFactory.getFunction(function.getText()).execute(sessionFunctionController);
            }
        }
    }
}
