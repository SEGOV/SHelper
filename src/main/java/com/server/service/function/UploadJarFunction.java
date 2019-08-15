package com.server.service.function;

import com.client.view.SessionFunctionController;
import com.server.model.ssh.Session;
import com.server.service.SessionService;
import com.server.service.file.FileService;

public class UploadJarFunction implements Function {

    @Override
    public void execute(SessionFunctionController sessionFunctionController) {
        updateIfNeededSessionProjectPath(sessionFunctionController);
        renameLocalProjectJars(sessionFunctionController);
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

    private void renameLocalProjectJars(SessionFunctionController sessionFunctionController) {
        FileService fileService = new FileService(sessionFunctionController);
        if (fileService.isSelectedToUploadJarsExist()) {
            fileService.renameSelectedJars();
        }
    }
}
