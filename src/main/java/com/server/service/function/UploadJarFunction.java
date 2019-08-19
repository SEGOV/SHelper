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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Objects;

import static com.server.Constants.Server.SERVER_IMPL_LIB_PATH;
import static com.server.Constants.Server.SERVER_WEB_LIB_PATH;

public class UploadJarFunction implements Function {
    private FileService fileService;
    private SessionAlert sessionAlert = SessionAlert.getInstance();

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
            System.out.println("Upload IMPL function commented");
//            upload(implJar, sessionFunctionController, SERVER_IMPL_LIB_PATH);
        }
        if(Objects.nonNull(webJar)) {
            System.out.println("Upload WEB function commented");
//            upload(webJar, sessionFunctionController, SERVER_WEB_LIB_PATH);
        }
    }

    private void upload(File jar, SessionFunctionController sessionFunctionController, String serverLibPath) {
        Session session = sessionFunctionController.getSession();
        SSHManager sshManager = SSHManager.getInstance();
        sshManager.fetchSSHManager(session);

        ChannelSftp sftpChannel = null;
        try {
            sftpChannel = sshManager.getSFTPChannelHome(serverLibPath);
        } catch (JSchException e) {
            sessionAlert.showConnectionFailed(sessionFunctionController.dialogStage);
            e.printStackTrace();
        }
        if (Objects.nonNull(sftpChannel)) {
            try {
                sftpChannel.put(new FileInputStream(jar), jar.getName(), ChannelSftp.OVERWRITE);
            } catch (SftpException e) {
                sessionAlert.showCanNotOverrideJarFile(sessionFunctionController.dialogStage, jar);
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                sessionAlert.showFileNotFoundUploadJar(sessionFunctionController.dialogStage, jar);
                e.printStackTrace();
            }
        } else {
            sessionAlert.showConnectionFailed(sessionFunctionController.dialogStage);
        }
    }
}
