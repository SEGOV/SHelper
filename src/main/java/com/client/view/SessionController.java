package com.client.view;

import com.client.alert.SessionAlert;
import com.jcraft.jsch.JSchException;
import com.server.model.ssh.SSHManager;
import com.server.model.ssh.Session;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import static com.server.Constants.Message.*;
import static com.server.Constants.Server.SERVER_HOME_PATH;

public class SessionController {
    @FXML
    protected TextField fileProtocolTextField;
    @FXML
    protected TextField hostNameTestField;
    @FXML
    protected TextField portNumberTextField;
    @FXML
    protected TextField userNameTextField;
    @FXML
    protected TextField passwordTextField;

    private SessionAlert sessionAlert = SessionAlert.getInstance();
    Stage dialogStage;
    protected Session session;

    @FXML
    protected void handleCancel() {
        dialogStage.close();
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

    boolean isInputValid() {
        String errorMessage = "";

        if (StringUtils.isEmpty(fileProtocolTextField.getText())) {
            errorMessage += NO_VALID_FILE_PROTOCOL + BREAK;
        }
        if (StringUtils.isEmpty(hostNameTestField.getText())) {
            errorMessage += NO_VALID_HOST_NAME + BREAK;
        }
        if (StringUtils.isEmpty(portNumberTextField.getText())) {
            errorMessage += NO_VALID_PORT_NUMBER + BREAK;
        } else {
            try {
                Integer.parseInt(portNumberTextField.getText());
            } catch (NumberFormatException e) {
                errorMessage += ONLY_DIGIT_VALID_FOR_PORT_NUMBER + BREAK;
            }
        }
        if (StringUtils.isEmpty(userNameTextField.getText())) {
            errorMessage += NO_VALID_USER_NAME + BREAK;
        }
        if (StringUtils.isEmpty(passwordTextField.getText())) {
            errorMessage += NO_VALID_PASSWORD + BREAK;
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            sessionAlert.showPleaseCorrectInvalidFieldsAlert(dialogStage, errorMessage);
            return false;
        }
    }

    boolean isSSHConnectionSuccess() {
        SSHManager sshManager = SSHManager.getInstance();
        sshManager.setHost(hostNameTestField.getText());
        sshManager.setPort(Integer.parseInt(portNumberTextField.getText()));
        sshManager.setUser(userNameTextField.getText());
        sshManager.setPassword(passwordTextField.getText());

        try {
            sshManager.getSFTPChannelHome(SERVER_HOME_PATH);
        } catch (JSchException e) {
            sessionAlert.showConnectionFailedWithParametersAlert(dialogStage);
            return false;
        }
        sessionAlert.showConnectionSuccessAlert(dialogStage);
        return true;
    }
}
