package com.server.service.file;

import com.client.alert.SessionAlert;
import com.client.view.controller.SessionFunctionController;
import com.server.exception.ShelperException;
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
    private SessionAlert sessionAlert = SessionAlert.getInstance();

    public FileService(SessionFunctionController sessionFunctionController) {
        this.sessionFunctionController = sessionFunctionController;
    }

    public void checkJarExist() throws ShelperException {
        Stage dialogStage = sessionFunctionController.dialogStage;
        String pathToProject = sessionFunctionController.pathToProjectsLabel.getText();

        if (Objects.isNull(pathToProject)) {
            throw new ShelperException();
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
                sessionAlert.showImplJarDoesntExistAlert(dialogStage, pathToProject);
                throw new ShelperException("Impl Jar doesn't exist");
            }
        }
        if (sessionFunctionController.webCheckBox.isSelected()) {
            String pathToWeb = "\\telenet-" + moduleName + "-web\\target\\telenet-" + moduleName + "-web-1.2.c2-SNAPSHOT.jar";
            String replacedPathToWeb = pathToWeb.replace("\\", "\\\\");
            String webJarPath = replacedPathToProject + replacedPathToWeb;
            webJar = new File(webJarPath);
            if (!webJar.exists()) {
                sessionAlert.showWebJarDoesntExistAlert(dialogStage, pathToProject);
                throw new ShelperException("Web Jar doesn't exist");
            }
        }
    }

    public void renameSelectedJars() {
        if (sessionFunctionController.implCheckBox.isSelected()) {
            String oldImplJarFilePath = implJar.getParent();
            File newImplJarFile = new File(oldImplJarFilePath + "\\" + "telenet-" + moduleName + "-impl.jar");
            if (!newImplJarFile.exists()) {
                newImplJarFile.delete();
            }
            implJar.renameTo(newImplJarFile); // TODO: Send event: "Rename Web jar file is success"
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

    public File getImplJar() {
        return implJar;
    }

    public File getWebJar() {
        return webJar;
    }
}
