package com.server.service.validator;

import com.client.alert.SessionAlert;
import com.client.view.SessionFunctionController;

public class FunctionInputValidationService {
    private SessionFunctionController sessionFunctionController;
    private SessionAlert sessionAlert = SessionAlert.getInstance();

    public FunctionInputValidationService(SessionFunctionController sessionFunctionController) {
        this.sessionFunctionController = sessionFunctionController;
    }

    public boolean isFunctionInputValid() {
        if (!sessionFunctionController.uploadJarsCheckBox.isSelected() & !sessionFunctionController.cleanBoilerCheckBox.isSelected() & !sessionFunctionController.restartServerCheckBox.isSelected()) {
            sessionAlert.showNotFunctionIsSelectedAlert(sessionFunctionController.dialogStage);
            return false;
        }
        if (sessionFunctionController.uploadJarsCheckBox.isSelected()) {
            boolean isPathToProjectEmpty = sessionFunctionController.pathToProjectsLabel.getText().isEmpty();
            if (isPathToProjectEmpty) {
                sessionAlert.showPathToProjectDirectoryIsEmptyAlert(sessionFunctionController.dialogStage);
                return false;
            } else if (!sessionFunctionController.implCheckBox.isSelected() & !sessionFunctionController.webCheckBox.isSelected()) {
                sessionAlert.showNoOneJarAreSelectedAlert(sessionFunctionController.dialogStage);
                return false;
            }
        }
        return true;
    }
}
