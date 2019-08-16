package com.client.alert;

import com.MainApp;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class SessionAlert {
    private static final SessionAlert INSTANCE = new SessionAlert();

    public static SessionAlert getInstance() {
        return INSTANCE;
    }

    public void showNoSessionSelectedAlert(MainApp mainApp) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("No Selection");
        alert.setHeaderText("No Session Selected");
        alert.setContentText("Please select a session in the table.");
        alert.showAndWait();
    }

    public void showConnectionFailed(Stage dialogStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Connection Failed");
        alert.setHeaderText("Network error: Network is unreachable");
        alert.showAndWait();
    }

    public void showNotFunctionIsSelectedAlert(Stage dialogStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Not function is selected");
        alert.setHeaderText("Please select function to action");
        alert.showAndWait();
    }

    public void showPathToProjectDirectoryIsEmptyAlert(Stage dialogStage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("Path to project directory is empty");
        alert.setHeaderText("Please choice path to project directory");
        alert.showAndWait();
    }

    public void showNoOneJarAreSelectedAlert(Stage dialogStage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("No one JAR are selected");
        alert.setHeaderText("Please choice JAR to upload");
        alert.showAndWait();
    }

    public void showImplJarDoesntExistAlert(Stage dialogStage, String pathToProject) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("Impl Jar doesn't exist");
        alert.setHeaderText("Selected Impl Jar to upload doesn't exist in the " + pathToProject + " directory");
        alert.showAndWait();
    }

    public void showWebJarDoesntExistAlert(Stage dialogStage, String pathToProject) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("Web Jar doesn't exist");
        alert.setHeaderText("Selected Web Jar to upload doesn't exist in the " + pathToProject + " directory");
        alert.showAndWait();
    }

    public void showConnectionFailedWithParametersAlert(Stage dialogStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Connection Failed");
        alert.setHeaderText("Network error: Network is unreachable");
        alert.setContentText("With this parameters connection is Filed");
        alert.showAndWait();
    }

    public void showConnectionSuccessAlert(Stage dialogStage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Connection");
        alert.setHeaderText("SFT Connection to Server is Success");
        alert.showAndWait();
    }

    public void showPleaseCorrectInvalidFieldsAlert(Stage dialogStage, String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(dialogStage);
        alert.setTitle("Invalid Fields");
        alert.setHeaderText("Please correct invalid fields");
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }
}
