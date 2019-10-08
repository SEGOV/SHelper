package com.server.service.function;

import com.client.view.controller.SessionFunctionController;
import com.server.Constants;
import com.server.service.file.FileService;

import java.io.File;
import java.net.URL;
import java.util.List;
import java.util.Objects;

import static com.server.Constants.Server.SERVER_JSP_PATH;

public class UploadJspFunction implements Function {
    @Override
    public void execute(SessionFunctionController sessionFunctionController) {
        List<String> allJspList = Constants.JSP.allJspList;

        for(String jspName : allJspList) {
            ClassLoader classLoader = UploadJspFunction.class.getClassLoader();
            String resourceFileUrl = Constants.JSP.JSP + jspName;
            URL resource = classLoader.getResource(resourceFileUrl);

            if (Objects.isNull(resource)) {
                sessionFunctionController.consoleAppendText(jspName + " is missed on the program, jsp will not be uploaded on the server.");
                new RuntimeException(jspName + " uploadFile missed on the program, jsp will not be uploaded on the server.", new Throwable());
            }
            File uploadFile = new File(resource.getFile());
            new FileService(sessionFunctionController).upload(uploadFile, SERVER_JSP_PATH);
            sessionFunctionController.consoleAppendText(jspName + " success uploaded on the server");
        }
    }
}
