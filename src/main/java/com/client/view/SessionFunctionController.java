package com.client.view;

import com.server.model.ssh.SSHCleanBoiler;
import com.server.model.ssh.Session;
import com.server.service.SessionService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class SessionFunctionController {
    @FXML
    private Label pathToProjectsLabel;
    @FXML
    private CheckBox implCheckBox;
    @FXML
    private CheckBox webCheckBox;
    @FXML
    private CheckBox uploadJarsCheckBox;
    @FXML
    private CheckBox cleanBoilerCheckBox;
    @FXML
    private CheckBox restartServerCheckBox;

    private boolean okClicked = false;
    private Stage dialogStage;
    private Session session;
    private SessionService sessionService;

    @FXML
    private void initialize() {
        sessionService = SessionService.getInstance();
    }

    @FXML
    private void handleSearch() {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        directoryChooser.setTitle("Select Idea Project Module Directory");
        directoryChooser.setInitialDirectory(new File(System.getProperty("user.home")));

        File dir = directoryChooser.showDialog(dialogStage);
        if (dir != null) {
            pathToProjectsLabel.setText(dir.getAbsolutePath());
        } else {
            pathToProjectsLabel.setText(null);
        }
    }

    @FXML
    private void handleClean() {
        pathToProjectsLabel.setText(null);
        implCheckBox.setSelected(false);
        webCheckBox.setSelected(false);
        uploadJarsCheckBox.setSelected(false);
        cleanBoilerCheckBox.setSelected(false);
        restartServerCheckBox.setSelected(false);
    }

    @FXML
    private void handleStart() {
        if (cleanBoilerCheckBox.isSelected()) {
            new SSHCleanBoiler().executeCleanCommand();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setSession(Session session) {
        this.session = session;
    }
}
