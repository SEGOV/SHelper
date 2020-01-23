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
    private SessionFunctionController sessionFunctionController;
    private String moduleName;
    private File renamedImplJar;
    private File renamedWebJar;
    private File oldImplJar;
    private File oldWebJar;
    private SessionAlert sessionAlert = SessionAlert.getInstance();

    public FileService(SessionFunctionController sessionFunctionController) {
        this.sessionFunctionController = sessionFunctionController;
    }

    public void upload(File uploadFile, String serverPathToUploadFile) {
        Session session = sessionFunctionController.getSession();
        SSHManager sshManager = SSHManager.getInstance();
        sshManager.fetchSSHManager(session);

        ChannelSftp sftpChannel = null;
        try {
            sftpChannel = sshManager.getSFTPChannel(serverPathToUploadFile);
        } catch (JSchException e) {
            sessionAlert.showConnectionFailed(sessionFunctionController.dialogStage);
            e.printStackTrace();
        }
        if (Objects.nonNull(sftpChannel)) {
            try {
                sftpChannel.put(new FileInputStream(uploadFile), uploadFile.getName(), ChannelSftp.OVERWRITE);
            } catch (SftpException e) {
                sessionAlert.showCanNotOverrideJarFile(sessionFunctionController.dialogStage, uploadFile);
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                sessionAlert.showFileNotFoundUploadJar(sessionFunctionController.dialogStage, uploadFile);
                e.printStackTrace();
            }
        } else {
            sessionAlert.showConnectionFailed(sessionFunctionController.dialogStage);
        }
    }

    public void renameJarsIfExists() {
        Stage dialogStage = sessionFunctionController.dialogStage;
        String pathToProject = sessionFunctionController.pathToProjectsLabel.getText();

        Pattern regex = Pattern.compile("[^.]+$");
        Matcher matcher = regex.matcher(pathToProject);
        if (matcher.find()) {
            moduleName = matcher.group(0).toLowerCase();
        }

        if (sessionFunctionController.implCheckBox.isSelected()) {
            String implJarPathDirectory = pathToProject + "\\telenet-" + moduleName + "-impl\\target\\";
            if (isImplJarExist(dialogStage, implJarPathDirectory)) {
                renameImplJar(implJarPathDirectory + "\\" + "telenet-" + moduleName + "-impl.jar");
            }
        }

        if (sessionFunctionController.webCheckBox.isSelected()) {
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
            this.oldImplJar = new File(oldImplJarName);
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
            this.oldWebJar = new File(oldWebJarName);
            return true;
        }
        return false;
    }

    private void renameImplJar(String implJarPath) {
        File newImplJarFile = new File(implJarPath);
        oldImplJar.renameTo(newImplJarFile); // TODO: Send event: "Rename Impl jar file is success"
        renamedImplJar = oldImplJar;
    }

    private void renameWebJar(String webJarPath) {
        File newWebJarFile = new File(webJarPath);
        oldWebJar.renameTo(newWebJarFile); // TODO: Send event: "Rename Web jar file is success"
        renamedWebJar = oldWebJar;
    }

    public File getRenamedImplJar() {
        return renamedImplJar;
    }

    public File getRenamedWebJar() {
        return renamedWebJar;
    }
}
