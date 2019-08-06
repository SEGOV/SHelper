package com.client.view;

import com.server.model.ssh.Session;
import com.server.service.SessionService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SessionEditController extends SessionController{
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
        if (isInputValid()/* & isSSHConnectionSuccess()*/) {
            session.setFileProtocol(fileProtocolTextField.getText());
            session.setHostName(hostNameTestField.getText());
            session.setPortNumber(Integer.parseInt(portNumberTextField.getText()));
            session.setUserName(userNameTextField.getText());
            session.setPassword(passwordTextField.getText());

            SessionService sessionService = SessionService.getInstance();
            sessionService.updateSession(session);
//            SessionService.getInstance().createSession(session);

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private void showConnectionFailed () {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Connection Failed");
        alert.setHeaderText("Network error: Network is unreachable");
        alert.setContentText("With this parameters connection is Filed");
        alert.showAndWait();
    }
}
