package com.server.service.validator;

import com.client.alert.SessionAlert;
import com.client.view.controller.SessionController;
import com.client.view.controller.SessionFunctionController;
import com.jcraft.jsch.JSchException;
import com.server.model.ssh.SSHManager;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

import static com.server.Constants.Message.*;
import static com.server.Constants.Message.CRLF;
import static com.server.Constants.Server.SERVER_HOME_PATH;

public class ValidationService {
    private SessionFunctionController sessionFunctionController;
    private SessionController sessionController;
    private SessionAlert sessionAlert = SessionAlert.getInstance();

    public ValidationService() {
    }

    public ValidationService(SessionFunctionController sessionFunctionController) {
        this.sessionFunctionController = sessionFunctionController;
    }

    public ValidationService(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public boolean isFunctionInputValid() {
        if (!sessionFunctionController.uploadJspCheckBox.isSelected() &
                !sessionFunctionController.uploadClassCheckBox.isSelected() &
                !sessionFunctionController.uploadJarsCheckBox.isSelected() &
                !sessionFunctionController.cleanBoilerCheckBox.isSelected() &
                !sessionFunctionController.restartServerCheckBox.isSelected()) {
            sessionAlert.showNotFunctionIsSelectedAlert(sessionFunctionController.dialogStage);
            return false;
        }
        if (sessionFunctionController.uploadJarsCheckBox.isSelected()) {
            String pathToProject = sessionFunctionController.pathToProjectsLabel.getText();
            if (Objects.isNull(pathToProject) || pathToProject.isEmpty()) {
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
            errorMessage += NO_VALID_FILE_PROTOCOL + CRLF;
        }
        if (StringUtils.isEmpty(sessionController.hostNameTestField.getText())) {
            errorMessage += NO_VALID_HOST_NAME + CRLF;
        }
        if (StringUtils.isEmpty(sessionController.portNumberTextField.getText())) {
            errorMessage += NO_VALID_PORT_NUMBER + CRLF;
        } else {
            try {
                Integer.parseInt(sessionController.portNumberTextField.getText());
            } catch (NumberFormatException e) {
                errorMessage += ONLY_DIGIT_VALID_FOR_PORT_NUMBER + CRLF;
            }
        }
        if (StringUtils.isEmpty(sessionController.userNameTextField.getText())) {
            errorMessage += NO_VALID_USER_NAME + CRLF;
        }
        if (StringUtils.isEmpty(sessionController.passwordTextField.getText())) {
            errorMessage += NO_VALID_PASSWORD + CRLF;
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
            sshManager.getSFTPChannel(SERVER_HOME_PATH);
        } catch (JSchException e) {
            sessionAlert.showConnectionFailedWithParametersAlert(dialogStage);
            return false;
        }
        sessionAlert.showConnectionSuccessAlert(dialogStage);
        return true;
    }

    public boolean isConfirmToExecuteFunctions(List<CheckBox> activeCheckBoxFunctionsList) {
        String confirmQuestionsMessage = TO_EXECUTE_CONFIRM_QUESTION + CRLF;
        StringBuilder selectedFunctionsToExecuteBuilder = new StringBuilder();
        selectedFunctionsToExecuteBuilder.append(confirmQuestionsMessage);
        activeCheckBoxFunctionsList.forEach(function -> selectedFunctionsToExecuteBuilder.append(" - " + function.getText() + CRLF));
        String resultQuestionToConfirmExecuteFunctionMessage = selectedFunctionsToExecuteBuilder.toString();
        return sessionAlert.isConfirmExecuteFunctionsDialog(resultQuestionToConfirmExecuteFunctionMessage);
    }
}
