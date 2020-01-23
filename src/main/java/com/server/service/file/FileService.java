package com.server.service.file;

import com.client.alert.SessionAlert;
import com.client.view.controller.SessionFunctionController;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.server.model.ssh.SSHManager;
import com.server.model.ssh.Session;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileService {

    private static final String SNAPSHOT_JAR = "SNAPSHOT.jar";
    private SessionFunctionController functionController;
    private String moduleName;
    private File newImplJarFile;
    private File newWebJarFile;
    private File implJar;
    private File webJar;
    private SessionAlert sessionAlert = SessionAlert.getInstance();

    public FileService(SessionFunctionController sessionFunctionController) {
        this.functionController = sessionFunctionController;
    }

    public void upload(File uploadFile, String serverPathToUploadFile) {
        Session session = functionController.getSession();
        SSHManager sshManager = SSHManager.getInstance();
        sshManager.fetchSSHManager(session);

        ChannelSftp sftpChannel = null;
        try {
            sftpChannel = sshManager.getSFTPChannel(serverPathToUploadFile);
        } catch (JSchException e) {
            sessionAlert.showConnectionFailed(functionController.dialogStage);
            e.printStackTrace();
        }
        if (Objects.nonNull(sftpChannel)) {
            try {
                sftpChannel.put(new FileInputStream(uploadFile), uploadFile.getName(), ChannelSftp.OVERWRITE);
            } catch (SftpException e) {
                sessionAlert.showCanNotOverrideJarFile(functionController.dialogStage, uploadFile);
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                sessionAlert.showFileNotFoundUploadJar(functionController.dialogStage, uploadFile);
                e.printStackTrace();
            }
        } else {
            sessionAlert.showConnectionFailed(functionController.dialogStage);
        }
        functionController.consoleAppendText("Success " + uploadFile.getName() + "  uploaded on the server.");
    }

    public void renameJarsIfExists() {
        Stage dialogStage = functionController.dialogStage;
        String pathToProject = functionController.pathToProjectsLabel.getText();

        Pattern regex = Pattern.compile("[^.]+$");
        Matcher matcher = regex.matcher(pathToProject);
        if (matcher.find()) {
            moduleName = matcher.group(0).toLowerCase();
        }

        if (functionController.implCheckBox.isSelected()) {
            String implJarPathDirectory = pathToProject + "\\telenet-" + moduleName + "-impl\\target\\";
            if (isImplJarExist(dialogStage, implJarPathDirectory)) {
                renameImplJar(implJarPathDirectory + "\\" + "telenet-" + moduleName + "-impl.jar");
            }
        }

        if (functionController.webCheckBox.isSelected()) {
            String webJarPathDirectory = pathToProject + "\\telenet-" + moduleName + "-web\\target\\";
            if (isWebJarExist(dialogStage, webJarPathDirectory)) {
                renameWebJar(webJarPathDirectory + "\\" + "telenet-" + moduleName + "-web.jar");
            }
        }
    }

    public boolean isImplJarExist(Stage dialogStage, String pathToProject) {
        File folder = new File(pathToProject);
        FilenameFilter txtFileFilter = (dir, name) -> {
            if (name.endsWith(SNAPSHOT_JAR)) {
                return true;
            } else {
                return false;
            }
        };
        File[] files = folder.listFiles(txtFileFilter);
        if (files.length == 0) {
            sessionAlert.showImplJarDoesntExistAlert(dialogStage, pathToProject);
        } else {
            String oldImplJarName = files[0].getAbsolutePath();
            this.implJar = new File(oldImplJarName);
            return true;
        }
        return false;
    }

    public boolean isWebJarExist(Stage dialogStage, String pathToProject) {
        File folder = new File(pathToProject);
        FilenameFilter txtFileFilter = (dir, name) -> {
            if (name.endsWith(SNAPSHOT_JAR)) {
                return true;
            } else {
                return false;
            }
        };
        File[] files = folder.listFiles(txtFileFilter);
        if (files.length == 0) {
            sessionAlert.showWebJarDoesntExistAlert(dialogStage, pathToProject);
        } else {
            String oldWebJarName = files[0].getAbsolutePath();
            this.webJar = new File(oldWebJarName);
            return true;
        }
        return false;
    }

    private void renameImplJar(String implJarPath) {
        newImplJarFile = new File(implJarPath);
        implJar.renameTo(newImplJarFile);
        functionController.consoleAppendText("Rename " + newImplJarFile.getName() + " success.");
    }

    private void renameWebJar(String webJarPath) {
        newWebJarFile = new File(webJarPath);
        webJar.renameTo(newWebJarFile);
        functionController.consoleAppendText("Rename " + newWebJarFile.getName() + " success.");
    }

    public File getRenamedImplJar() {
        return newImplJarFile;
    }

    public File getRenamedWebJar() {
        return newWebJarFile;
    }
}
