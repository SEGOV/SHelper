package com.client.view;

import com.jcraft.jsch.JSchException;
import com.server.model.ssh.SSHManager;
import com.server.model.ssh.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

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
            errorMessage += "No valid File Protocol!\n";
        }
        if (StringUtils.isEmpty(hostNameTestField.getText())) {
            errorMessage += "No valid Host Name!\n";
        }
        if (StringUtils.isEmpty(portNumberTextField.getText())) {
            errorMessage += "No valid Port Number!\n";
        } else {
            try {
                Integer.parseInt(portNumberTextField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Only digit valid for Port Number!\n";
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

    boolean isSSHConnectionSuccess() {
        SSHManager sshManager = SSHManager.getInstance();
        sshManager.setHost(hostNameTestField.getText());
        sshManager.setPort(Integer.parseInt(portNumberTextField.getText()));
        sshManager.setUser(userNameTextField.getText());
        sshManager.setPassword(passwordTextField.getText());

        try {
            sshManager.getSFTPChannel();
        } catch (JSchException e) {
            showConnectionFailed();
            return false;
        }
        showConnectionSuccess();
        return true;
    }

    private void showConnectionFailed() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Connection Failed");
        alert.setHeaderText("Network error: Network is unreachable");
        alert.setContentText("With this parameters connection is Filed");
        alert.showAndWait();
    }

    private void showConnectionSuccess() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Connection");
        alert.setHeaderText("SFT Connection to Server is Success");
        alert.showAndWait();
    }
}
