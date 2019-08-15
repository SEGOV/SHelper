package com.client.view;

import com.server.model.ssh.Session;
import com.server.service.function.FunctionService;
import com.server.service.validator.FunctionValidationService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

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

    @FXML
    private void initialize() {
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

        if (isUploadJarFunctionInputValid) {
            List<CheckBox> functionsList = new LinkedList();
            functionsList.add(uploadJarsCheckBox);
            functionsList.add(cleanBoilerCheckBox);
            functionsList.add(restartServerCheckBox);
            FunctionService.getInstance().executeFunctions(functionsList, this);

            System.out.println("ALL FINE");
        } else {
            System.out.println("BAD");
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

    public Session getSession() {
        return session;
    }

    public void setSession(Session session) {
        this.session = session;
        pathToProjectsLabel.setText(session.getProjectPath());
    }
}
