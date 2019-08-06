package com.client.view;

import com.jcraft.jsch.JSchException;
import com.server.model.ssh.SSHManager;
import javafx.scene.control.Alert;
import org.apache.commons.lang3.StringUtils;

public class SessionController {
    protected boolean isInputValid() {
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

    protected boolean isSSHConnectionSuccess() {
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
        return true;
    }

    protected void showConnectionFailed () {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(dialogStage);
        alert.setTitle("Connection Failed");
        alert.setHeaderText("Network error: Network is unreachable");
        alert.setContentText("With this parameters connection is Filed");
        alert.showAndWait();
    }
}
