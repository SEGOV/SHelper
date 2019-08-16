package com.client.view;

import com.server.model.ssh.Session;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SessionController {
    @FXML
    public TextField fileProtocolTextField;
    @FXML
    public TextField hostNameTestField;
    @FXML
    public TextField portNumberTextField;
    @FXML
    public TextField userNameTextField;
    @FXML
    public TextField passwordTextField;

    Stage dialogStage;
    Session session;

    @FXML
    protected void handleCancel() {
        dialogStage.close();
    }

    public Stage getDialogStage() {
        return dialogStage;
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

    void fetchSession() {
        session.setFileProtocol(fileProtocolTextField.getText());
        session.setHostName(hostNameTestField.getText());
        session.setPortNumber(Integer.parseInt(portNumberTextField.getText()));
        session.setUserName(userNameTextField.getText());
        session.setPassword(passwordTextField.getText());
    }

    public Session getSession() {
        return session;
    }
}
