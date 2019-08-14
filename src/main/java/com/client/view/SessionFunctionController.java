package com.client.view;

import com.server.model.ssh.SSHCleanBoiler;
import com.server.model.ssh.Session;
import com.server.service.SessionService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    private File implJar;
    private File webJar;

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
        if(isUploadJarFunctionInputValid() & isSelectedToUploadJarsExist()) {
            System.out.println("ALL FINE");
        } else {
            System.out.println("BAD");
        }
        if (cleanBoilerCheckBox.isSelected()) {
            new SSHCleanBoiler().executeCleanCommand();
        }
    }

    private boolean isUploadJarFunctionInputValid() {
        if(!uploadJarsCheckBox.isSelected() & !cleanBoilerCheckBox.isSelected() & !restartServerCheckBox.isSelected()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(dialogStage);
            alert.setTitle("Not function is selected");
            alert.setHeaderText("Please select function to action");
            alert.showAndWait();
            return false;
        }
        if(uploadJarsCheckBox.isSelected()) {
            boolean isPathToProjectEmpty = pathToProjectsLabel.getText().isEmpty();
            if(isPathToProjectEmpty) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("Path to project directory is empty");
                alert.setHeaderText("Please choice path to project directory");
                alert.showAndWait();
                return false;
            }else if(!implCheckBox.isSelected() & !webCheckBox.isSelected()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("No one JAR are selected");
                alert.setHeaderText("Please choice JAR to upload");
                alert.showAndWait();
                return false;
            }
        }
        return true;
    }

    private boolean isSelectedToUploadJarsExist() {
        String pathToProject = pathToProjectsLabel.getText();
        String moduleName = null;

        Pattern regex = Pattern.compile("[^.]+$");
        Matcher matcher = regex.matcher(pathToProject);
        if (matcher.find()) {
            moduleName = matcher.group(0).toLowerCase();
        }

        String replacedPathToProject = pathToProject.replace("\\", "\\\\");
        if(implCheckBox.isSelected()) {
            String pathToImpl = "\\telenet-" + moduleName + "-impl\\target\\telenet-" + moduleName + "-impl-1.2.c2-SNAPSHOT.jar";
            String replacedPathToImpl = pathToImpl.replace("\\", "\\\\");
            String implJarPath = replacedPathToProject + replacedPathToImpl;
            implJar = new File(implJarPath);
            if(!implJar.exists()) {
                 Alert alert = new Alert(Alert.AlertType.ERROR);
                 alert.initOwner(dialogStage);
                 alert.setTitle("Impl Jar doesn't exist");
                 alert.setHeaderText("Selected Impl Jar to upload doesn't exist in the " + pathToProject + " directory");
                 alert.showAndWait();
                 return false;
             }
        }
        if(webCheckBox.isSelected()) {
            String pathToWeb = "\\telenet-" + moduleName + "-web\\target\\telenet-" + moduleName + "-web-1.2.c2-SNAPSHOT.jar";
            String replacedPathToWeb = pathToWeb.replace("\\", "\\\\");
            String webJarPath = replacedPathToProject + replacedPathToWeb;
            webJar = new File(webJarPath);
            if(!webJar.exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("Web Jar doesn't exist");
                alert.setHeaderText("Selected Web Jar to upload doesn't exist in the " + pathToProject + " directory");
                alert.showAndWait();
                return false;
            }
        }
        return true;
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
