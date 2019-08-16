package com.server.service.function;

import com.client.alert.SessionAlert;
import com.client.view.SessionFunctionController;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSchException;
import com.server.exception.ShelperException;
import com.server.model.ssh.SSHManager;
import com.server.model.ssh.Session;
import com.server.service.SessionService;
import com.server.service.file.FileService;

import java.io.File;
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
        ChannelSftp sftpChannel = null;
        try {
            sftpChannel = new SSHManager().getSFTPChannel();
        } catch (JSchException e) {
            SessionAlert.getInstance().showConnectionFailed(sessionFunctionController.dialogStage);
        }

        if (Objects.nonNull(sftpChannel)) {
//            sftpChannel.put(new FileInputStream(f1), f1.getName(), ChannelSftp.OVERWRITE);
        } else {
            SessionAlert.getInstance().showConnectionFailed(sessionFunctionController.dialogStage);
        }
    }
}
