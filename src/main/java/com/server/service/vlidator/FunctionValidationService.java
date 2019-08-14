package com.server.service.vlidator;

import com.client.view.SessionFunctionController;
import javafx.scene.control.Alert;

public class FunctionValidationService {
    private SessionFunctionController sessionFunctionController;

    public FunctionValidationService(SessionFunctionController sessionFunctionController) {
        this.sessionFunctionController = sessionFunctionController;
    }

    public boolean isUploadJarFunctionInputValid() {
        if (!sessionFunctionController.uploadJarsCheckBox.isSelected() & !sessionFunctionController.cleanBoilerCheckBox.isSelected() & !sessionFunctionController.restartServerCheckBox.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(sessionFunctionController.dialogStage);
            alert.setTitle("Not function is selected");
            alert.setHeaderText("Please select function to action");
            alert.showAndWait();
            return false;
        }
        if (sessionFunctionController.uploadJarsCheckBox.isSelected()) {
            boolean isPathToProjectEmpty = sessionFunctionController.pathToProjectsLabel.getText().isEmpty();
            if (isPathToProjectEmpty) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(sessionFunctionController.dialogStage);
                alert.setTitle("Path to project directory is empty");
                alert.setHeaderText("Please choice path to project directory");
                alert.showAndWait();
                return false;
            } else if (!sessionFunctionController.implCheckBox.isSelected() & !sessionFunctionController.webCheckBox.isSelected()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(sessionFunctionController.dialogStage);
                alert.setTitle("No one JAR are selected");
                alert.setHeaderText("Please choice JAR to upload");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }
}
