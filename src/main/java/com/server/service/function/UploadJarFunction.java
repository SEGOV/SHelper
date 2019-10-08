package com.server.service.function;

import com.client.view.controller.SessionFunctionController;
import com.server.exception.ShelperException;
import com.server.model.ssh.Session;
import com.server.service.SessionService;
import com.server.service.file.FileService;

import java.io.File;
import java.util.Objects;

import static com.server.Constants.Server.SERVER_IMPL_LIB_PATH;

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
        fileService.checkJarExist();
        fileService.renameSelectedJars();
    }

    private void uploadJarsToServer() {
        File implJar = fileService.getImplJar();
        File webJar = fileService.getWebJar();

        if(Objects.nonNull(implJar)) {
            System.out.println("Upload IMPL function commented");
            fileService.upload(implJar, SERVER_IMPL_LIB_PATH);
        }
        if(Objects.nonNull(webJar)) {
            System.out.println("Upload WEB function commented");
//            upload(webJar, SERVER_WEB_LIB_PATH);
        }
    }
}
