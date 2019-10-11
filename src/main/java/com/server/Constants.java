package com.server;

import java.util.Arrays;
import java.util.List;

public class Constants {
    public interface Icon {
        String PROJECT_ICON_IMAGE_PATH = "/icons/plus.PNG";
    }

    public interface Session {
        String SFTP_FILE_PROTOCOL = "SFTP";
        Integer PORT = 22;
        Integer TIMEOUT = 3000;
    }

    public interface Server {
        String SERVER_HOME_PATH = "/u02/netcracker/toms/u141_dev_6300";
        String SERVER_IMPL_LIB_PATH = "/u02/netcracker/toms/u141_dev_6300/applications/NetCracker/APP-INF/lib";
        String SERVER_WEB_LIB_PATH = "/u02/netcracker/toms/u141_dev_6300/applications/NetCracker/NetCrackerWebApp/WEB-INF/lib";
        String SERVER_JSP_PATH = "/u02/netcracker/toms/u141_dev_6300/applications/NetCracker/NetCrackerWebApp/tools";
    }

    public interface Boiler {
        String CLEAN_BOILER_COMMAND_NAME = "Clean Boiler";
        String CLEAN_BOILER_SCRIPT_NAME = "clean_boiler.sh";
        String CLEAN_BOILER_COMMAND = "sh " + CLEAN_BOILER_SCRIPT_NAME;
        String CLEAN_BOILER_SCRIPT_PATH = "scripts/clean_boiler.sh";
    }

    public interface JSP {
        String JSP = "jsp/";
        String RBM_EXECUTOR_JSP_PATH = "RBMExecutorLite.jsp";
        String SHOW_GRANTS_EXTENDED_LIST_JSP_PATH = "showGrantsExtendedList.jsp";

        List<String> allJspList = Arrays.asList(RBM_EXECUTOR_JSP_PATH, SHOW_GRANTS_EXTENDED_LIST_JSP_PATH);
    }

    public interface Message {
        String CRLF = "\n"; //Carriage Return and Line Feed
        String EMDASH = "---------------------------------------------";
        String NO_VALID_FILE_PROTOCOL = "No valid File Protocol!";
        String NO_VALID_HOST_NAME = "No valid Host Name!";
        String NO_VALID_PORT_NUMBER = "No valid Port Number!";
        String ONLY_DIGIT_VALID_FOR_PORT_NUMBER = "Only digit valid for Port Number!";
        String NO_VALID_USER_NAME = "No valid User Name!";
        String NO_VALID_PASSWORD = "No valid password!";

        String TO_EXECUTE_CONFIRM_QUESTION = "Are you sure to execute these functions? ";
    }
}
