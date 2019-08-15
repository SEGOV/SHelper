package com.server.service.file;

import com.client.view.SessionFunctionController;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.io.File;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileService {

    private SessionFunctionController sessionFunctionController;
    private String moduleName;
    private File implJar;
    private File webJar;

    public FileService(SessionFunctionController sessionFunctionController) {
        this.sessionFunctionController = sessionFunctionController;
    }

    public boolean isSelectedToUploadJarsExist() {
        Stage dialogStage = sessionFunctionController.dialogStage;
        String pathToProject = sessionFunctionController.pathToProjectsLabel.getText();

        if (Objects.isNull(pathToProject)) {
            return false;
        }

        Pattern regex = Pattern.compile("[^.]+$");
        Matcher matcher = regex.matcher(pathToProject);
        if (matcher.find()) {
            moduleName = matcher.group(0).toLowerCase();
        }

        String replacedPathToProject = pathToProject.replace("\\", "\\\\");
        if (sessionFunctionController.implCheckBox.isSelected()) {
            String pathToImpl = "\\telenet-" + moduleName + "-impl\\target\\telenet-" + moduleName + "-impl-1.2.c2-SNAPSHOT.jar";
            String replacedPathToImpl = pathToImpl.replace("\\", "\\\\");
            String implJarPath = replacedPathToProject + replacedPathToImpl;
            implJar = new File(implJarPath);
            if (!implJar.exists()) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initOwner(dialogStage);
                alert.setTitle("Impl Jar doesn't exist");
                alert.setHeaderText("Selected Impl Jar to upload doesn't exist in the " + pathToProject + " directory");
                alert.showAndWait();
                return false;
            }
        }
        if (sessionFunctionController.webCheckBox.isSelected()) {
            String pathToWeb = "\\telenet-" + moduleName + "-web\\target\\telenet-" + moduleName + "-web-1.2.c2-SNAPSHOT.jar";
            String replacedPathToWeb = pathToWeb.replace("\\", "\\\\");
            String webJarPath = replacedPathToProject + replacedPathToWeb;
            webJar = new File(webJarPath);
            if (!webJar.exists()) {
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

    public void renameSelectedJars() {
        if (sessionFunctionController.implCheckBox.isSelected()) {
            String oldImplJarFilePath = webJar.getParent();
            File newImplJarFile = new File(oldImplJarFilePath + "\\" + "telenet-" + moduleName + "-impl.jar");
            if (!newImplJarFile.exists()) {
                newImplJarFile.delete();
            }
            webJar.renameTo(newImplJarFile); // TODO: Send event: "Rename Web jar file is success"
            implJar = newImplJarFile;
        }
        if (sessionFunctionController.webCheckBox.isSelected()) {
            String oldWebJarFilePath = webJar.getParent();
            File newWebJarFile = new File(oldWebJarFilePath + "\\" + "telenet-" + moduleName + "-web.jar");
            if (newWebJarFile.exists()) {
                newWebJarFile.delete();
            }
            webJar.renameTo(newWebJarFile);  // TODO: Send event: "Rename Web jar file is success"
            webJar = newWebJarFile;
        }
    }
}
