package com.server.service.validator;

import com.client.alert.SessionAlert;
import com.client.view.SessionController;
import com.client.view.SessionFunctionController;
import com.jcraft.jsch.JSchException;
import com.server.model.ssh.SSHManager;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import static com.server.Constants.Message.*;
import static com.server.Constants.Message.BREAK;
import static com.server.Constants.Server.SERVER_HOME_PATH;

public class ValidationService {
    private SessionFunctionController sessionFunctionController;
    private SessionController sessionController;
    private SessionAlert sessionAlert = SessionAlert.getInstance();

    public ValidationService(SessionFunctionController sessionFunctionController) {
        this.sessionFunctionController = sessionFunctionController;
    }

    public ValidationService(SessionController sessionController) {
        this.sessionController = sessionController;
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

    public boolean isInputValid() {
        String errorMessage = "";

        if (StringUtils.isEmpty(sessionController.fileProtocolTextField.getText())) {
            errorMessage += NO_VALID_FILE_PROTOCOL + BREAK;
        }
        if (StringUtils.isEmpty(sessionController.hostNameTestField.getText())) {
            errorMessage += NO_VALID_HOST_NAME + BREAK;
        }
        if (StringUtils.isEmpty(sessionController.portNumberTextField.getText())) {
            errorMessage += NO_VALID_PORT_NUMBER + BREAK;
        } else {
            try {
                Integer.parseInt(sessionController.portNumberTextField.getText());
            } catch (NumberFormatException e) {
                errorMessage += ONLY_DIGIT_VALID_FOR_PORT_NUMBER + BREAK;
            }
        }
        if (StringUtils.isEmpty(sessionController.userNameTextField.getText())) {
            errorMessage += NO_VALID_USER_NAME + BREAK;
        }
        if (StringUtils.isEmpty(sessionController.passwordTextField.getText())) {
            errorMessage += NO_VALID_PASSWORD + BREAK;
        }
        if (errorMessage.length() == 0) {
            return true;
        } else {
            sessionAlert.showPleaseCorrectInvalidFieldsAlert(sessionController.getDialogStage(), errorMessage);
            return false;
        }
    }

    public boolean isSSHConnectionSuccess() {
        Stage dialogStage = sessionController.getDialogStage();
        SSHManager sshManager = SSHManager.getInstance();
        sshManager.fetchSSHManager(sessionController.getSession());
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
