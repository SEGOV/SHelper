package com.client.view;

import com.MainApp;
import com.server.model.ssh.Session;
import com.server.service.SessionService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

public class SessionEditController {
    @FXML
    private TextField fileProtocolTextField;
    @FXML
    private TextField hostNameTestField;
    @FXML
    private TextField portNumberTextField;
    @FXML
    private TextField userNameTextField;
    @FXML
    private TextField passwordTextField;

    private MainApp mainApp;
    private Stage dialogStage;
    private Session session;
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setSession(Session session) {
        this.session = session;

        fileProtocolTextField.setText(session.getFileProtocol());
        hostNameTestField.setText(session.getHostName());
        portNumberTextField.setText(String.valueOf(session.getPortNumber()));
        userNameTextField.setText(session.getUserName());
        passwordTextField.setText(session.getPassword());
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        if (isInputValid()) {
            session.setFileProtocol(fileProtocolTextField.getText());
            session.setHostName(hostNameTestField.getText());
            session.setPortNumber(Integer.parseInt(portNumberTextField.getText()));
            session.setUserName(userNameTextField.getText());
            session.setPassword(passwordTextField.getText());
            SessionService.getInstance().createSession(session);

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (StringUtils.isEmpty(fileProtocolTextField.getText())) {
            errorMessage += "No valid File Protocol!\n";
        }
        if (StringUtils.isEmpty(hostNameTestField.getText())) {
            errorMessage += "No valid Host Name!\n";
        }
        if (StringUtils.isEmpty(portNumberTextField.getText())) {
            errorMessage += "No valid Port Number!\n";
        }
        if (true) {
            try {
                Integer.parseInt(portNumberTextField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid Port Number!\n";
            }
        }
        if (StringUtils.isEmpty(userNameTextField.getText())) {
            errorMessage += "No valid User Name!\n";
        }
        if (StringUtils.isEmpty(passwordTextField.getText())) {
            errorMessage += "No valid password!\n";
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);

            alert.showAndWait();
            return false;
        }
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }
}
