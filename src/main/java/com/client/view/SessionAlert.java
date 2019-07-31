package com.client.view;

import com.MainApp;

public class SessionAlert {
    private static final SessionAlert INSTANCE = new SessionAlert();

    public static SessionAlert getInstance() {
        return INSTANCE;
    }

    public void showNoSessionSelectedAlert(MainApp mainApp) {
        javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.WARNING);
        alert.initOwner(mainApp.getPrimaryStage());
        alert.setTitle("No Selection");
        alert.setHeaderText("No Session Selected");
        alert.setContentText("Please select a session in the table.");
        alert.showAndWait();
    }
}
