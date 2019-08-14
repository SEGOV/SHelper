package com.client.view;

import com.server.model.ssh.SSHCleanBoiler;
import com.server.model.ssh.Session;
import com.server.service.SessionService;
import com.server.service.file.FileService;
import com.server.service.vlidator.FunctionValidationService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;

public class SessionFunctionController {
    @FXML
    public Label pathToProjectsLabel;
    @FXML
    public CheckBox implCheckBox;
    @FXML
    public CheckBox webCheckBox;
    @FXML
    public CheckBox uploadJarsCheckBox;
    @FXML
    public CheckBox cleanBoilerCheckBox;
    @FXML
    public CheckBox restartServerCheckBox;

    public Stage dialogStage;
    private boolean okClicked = false;
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
    private void handleStart() {
        boolean isUploadJarFunctionInputValid = new FunctionValidationService(this).isUploadJarFunctionInputValid();
        FileService fileService = new FileService(this);
        boolean isSelectedToUploadJarsExist = fileService.isSelectedToUploadJarsExist();

        if (isUploadJarFunctionInputValid & isSelectedToUploadJarsExist) {
            System.out.println("ALL FINE");
            fileService.renameSelectedJars();

        } else {
            System.out.println("BAD");
        }
        if (cleanBoilerCheckBox.isSelected()) {
            new SSHCleanBoiler().executeCleanCommand();
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
