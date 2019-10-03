package com.client.view.controller;

import com.server.model.ssh.Session;
import com.server.service.SessionService;
import com.server.service.validator.ValidationService;
import javafx.fxml.FXML;

import java.util.Objects;

public class SessionEditController extends SessionController {
    private boolean okClicked = false;

    @FXML
    private void initialize() {
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleOk() {
        SessionService sessionService = SessionService.getInstance();
        if (Objects.isNull(session.getId())) {
            Session sessionByParameters = sessionService.getSessionByParameters(session);
            Integer sessionByParametersId = sessionByParameters.getId();
            session.setId(sessionByParametersId);
        }
        ValidationService validationService = new ValidationService(this);
        boolean isInputValid = validationService.isInputValid();
        if (isInputValid) {
            fetchSession();
            boolean isSSHConnectionSuccess = validationService.isSSHConnectionSuccess();
            if(isSSHConnectionSuccess) {
                sessionService.updateSession(session);
                okClicked = true;
                dialogStage.close();
            }
        }
    }
}
