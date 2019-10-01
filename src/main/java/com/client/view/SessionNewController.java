package com.client.view;

import com.client.alert.SessionAlert;
import com.server.service.SessionService;
import com.server.service.validator.ValidationService;
import javafx.fxml.FXML;

public class SessionNewController extends SessionController {
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        ValidationService validationService = new ValidationService(this);
        boolean isInputValid = validationService.isInputValid();
        if (isInputValid) {
            fetchSession();
            SessionService sessionService = SessionService.getInstance();
            int countSessionByHostName = sessionService.getCountSessionByHostName(session.getHostName());
            if(countSessionByHostName > 0) {
                SessionAlert.getInstance().showSessionExistAlert(dialogStage);
                return;
            }
            boolean isSSHConnectionSuccess = validationService.isSSHConnectionSuccess();
            if(isSSHConnectionSuccess) {
                sessionService.createSession(session);
                okClicked = true;
                dialogStage.close();
            }
        }
    }
}
