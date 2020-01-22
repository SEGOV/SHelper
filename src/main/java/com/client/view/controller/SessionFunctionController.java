package com.client.view.controller;

import com.server.exception.ShelperException;
import com.server.model.ssh.Session;
import com.server.service.function.FunctionService;
import com.server.service.validator.ValidationService;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.DragEvent;
import javafx.scene.input.TransferMode;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;

import static com.server.Constants.Message.*;

public class SessionFunctionController {
    @FXML
    public Label pathToProjectsLabel;
    @FXML
    public CheckBox implCheckBox;
    @FXML
    public CheckBox webCheckBox;
    @FXML
    public Label drugAndDropLabel;
    @FXML
    public CheckBox uploadJspCheckBox;
    @FXML
    public CheckBox uploadClassCheckBox;
    @FXML
    public CheckBox uploadJarsCheckBox;
    @FXML
    public CheckBox cleanBoilerCheckBox;
    @FXML
    public CheckBox restartServerCheckBox;
    @FXML
    public TextArea console;

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

        File projectFile = directoryChooser.showDialog(dialogStage);
        if (projectFile != null) {
            pathToProjectsLabel.setText(projectFile.getAbsolutePath());
        } else {
            pathToProjectsLabel.setText(null);
        }
        drugAndDropLabel.setText(DRAG_AND_DROP_CLASS_FILE);
    }

    @FXML
    private void handleStart() throws ShelperException {
        boolean isFunctionInputValid = new ValidationService(this).isFunctionInputValid();

        if (isFunctionInputValid) {
            console.clear();
            List<CheckBox> functionsList = new LinkedList();
            functionsList.add(uploadJspCheckBox);
            functionsList.add(uploadClassCheckBox);
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
        uploadJspCheckBox.setSelected(false);
        uploadClassCheckBox.setSelected(false);
        uploadJarsCheckBox.setSelected(false);
        cleanBoilerCheckBox.setSelected(false);
        restartServerCheckBox.setSelected(false);
        drugAndDropLabel.setText(DRAG_AND_DROP_CLASS_FILE);
    }

    @FXML
    private void handleCleanConsole() {
        console.clear();
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

    public void consoleAppendText(String message) {
        this.console.appendText(message + CRLF + EMDASH + CRLF);
    }

    public void handleDragOver(DragEvent event) {
        if(event.getDragboard().hasFiles()) {
            event.acceptTransferModes(TransferMode.ANY);
        }
    }

    public void handleDrop(DragEvent event) throws FileNotFoundException {
        event.acceptTransferModes(TransferMode.ANY);
        File file = event.getDragboard().getFiles().get(0);
        FileInputStream fileInputStream = new FileInputStream(file); // TODO: as soon
        drugAndDropLabel.setText(file.getName());
        System.out.println(file.getName());
    }
}
