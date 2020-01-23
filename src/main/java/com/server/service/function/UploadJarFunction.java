package com.server.service.function;

import com.client.view.controller.SessionFunctionController;
import com.server.exception.ShelperException;
import com.server.model.ssh.Session;
import com.server.service.SessionService;
import com.server.service.file.FileService;

import java.io.File;
import java.util.Objects;

public class UploadJarFunction implements Function {
    private FileService fileService;
    private SessionFunctionController sessionFunctionController;

    @Override
    public void execute(SessionFunctionController sessionFunctionController) throws ShelperException {
        this.sessionFunctionController = sessionFunctionController;
        updateIfNeededSessionProjectPath();
        renameLocalProjectJars();
        uploadJarsToServer();
    }

    private void updateIfNeededSessionProjectPath() {
        Session session = sessionFunctionController.getSession();
        String sessionProjectPath = session.getProjectPath();
        String inputProjectPath = sessionFunctionController.pathToProjectsLabel.getText();

        if (!inputProjectPath.equals(sessionProjectPath)) {
            session.setProjectPath(inputProjectPath);
            SessionService.getInstance().updateSession(session);
        }
    }

    private void renameLocalProjectJars() throws ShelperException {
        fileService = new FileService(sessionFunctionController);
        fileService.renameJarsIfExists();
    }

    private void uploadJarsToServer() {
        File implJar = fileService.getRenamedImplJar();
        File webJar = fileService.getRenamedWebJar();

        if(Objects.nonNull(implJar)) {
//            fileService.upload(implJar, SERVER_IMPL_LIB_PATH);
        }
        if(Objects.nonNull(webJar)) {
//            upload(webJar, SERVER_WEB_LIB_PATH);
        }
    }
}
