package com.server.service.function;

import com.client.alert.SessionAlert;
import com.client.view.SessionFunctionController;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.SftpException;
import com.server.exception.ShelperException;
import com.server.model.ssh.SSHManager;
import com.server.model.ssh.Session;
import com.server.service.SessionService;
import com.server.service.file.FileService;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

public class UploadJarFunction implements Function {
    private FileService fileService;

    @Override
    public void execute(SessionFunctionController sessionFunctionController) throws ShelperException {
        updateIfNeededSessionProjectPath(sessionFunctionController);
        renameLocalProjectJars(sessionFunctionController);
        uploadJarsToServer(sessionFunctionController);
    }

    private void updateIfNeededSessionProjectPath(SessionFunctionController sessionFunctionController) {
        Session session = sessionFunctionController.getSession();
        String sessionProjectPath = session.getProjectPath();
        String inputProjectPath = sessionFunctionController.pathToProjectsLabel.getText();
        if (!inputProjectPath.equals(sessionProjectPath)) {
            session.setProjectPath(inputProjectPath);
            SessionService.getInstance().updateSession(session);
        }
    }

    private void renameLocalProjectJars(SessionFunctionController sessionFunctionController) throws ShelperException {
        fileService = new FileService(sessionFunctionController);
        fileService.checkJarExist();
        fileService.renameSelectedJars();
    }

    private void uploadJarsToServer(SessionFunctionController sessionFunctionController) {
        File implJar = fileService.getImplJar();
        File webJar = fileService.getWebJar();

        if(Objects.nonNull(implJar)) {
            upload(implJar, sessionFunctionController.dialogStage);
        }
        if(Objects.nonNull(webJar)) {
            upload(webJar, sessionFunctionController.dialogStage);
        }
    }

    private void upload(File jar, Stage dialogStage) {
        ChannelSftp sftpChannel = null;
        try {
            SSHManager sshManager = SSHManager.getInstance();
            sftpChannel = sshManager.getSFTPChannel();
            System.out.println("sftpChannel: " + sftpChannel);
        } catch (JSchException e) {
            SessionAlert.getInstance().showConnectionFailed(dialogStage);
            e.printStackTrace();
        }

//        try {
//            System.out.println("pwd : " + sftpChannel.pwd());
//        } catch (SftpException e) {
//            e.printStackTrace();
//        }


        if (Objects.nonNull(sftpChannel)) {
//            try {
//                sftpChannel.put(new FileInputStream(jar), jar.getName(), ChannelSftp.OVERWRITE);
//            } catch (SftpException e) {
//                e.printStackTrace();
//            } catch (FileNotFoundException e) {
//                e.printStackTrace();
//            }
        } else {
//            SessionAlert.getInstance().showConnectionFailed(dialogStage);
        }
    }
}
