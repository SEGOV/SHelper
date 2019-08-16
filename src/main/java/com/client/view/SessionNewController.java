package com.client.view;

import com.server.service.SessionService;
import com.server.service.validator.InputValidationService;
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
        boolean isInputValid = new InputValidationService(this).isInputValid();
        if (isInputValid && isSSHConnectionSuccess()) {
            fetchSession();
            SessionService.getInstance().createSession(session);
            okClicked = true;
            dialogStage.close();
        }
    }
}
