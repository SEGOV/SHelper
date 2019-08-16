package com.server.model.ssh;

import com.client.view.SessionFunctionController;
import com.jcraft.jsch.*;
import com.jcraft.jsch.Session;

import java.io.*;
import java.net.URL;
import java.util.Objects;

import static com.server.Constants.Boiler.CLEAN_BOILER_SCRIPT_NAME;
import static com.server.Constants.Boiler.CLEAN_BOILER_SCRIPT_PATH;
import static com.server.Constants.Server.SERVER_HOME_PATH;

public class SSHCleanBoiler {
    public void executeCleanCommand(SessionFunctionController sessionFunctionController) {
        SSHManager sshManager = SSHManager.getInstance();
        com.server.model.ssh.Session session = sessionFunctionController.getSession();
        sshManager.fetchSSHManager(session);
        Session sftpSession;
        try {
            ChannelSftp sftpChannel = SSHManager.getInstance().getSFTPChannelHome(SERVER_HOME_PATH);
            System.out.println("CLEAN BOILER PATH: " + sftpChannel.pwd());
            sftpSession = sftpChannel.getSession();

            uploadScript(sftpChannel);

            ChannelExec channelExec = (ChannelExec)sftpSession.openChannel("exec");
            InputStream in = channelExec.getInputStream();

            channelExec.setCommand("sh clean_boiler.sh");
            channelExec.connect();

            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            int index = 0;

            while ((line = reader.readLine()) != null)
            {
                System.out.println(++index + " : " + line);
            }

            channelExec.disconnect();
            sftpSession.disconnect();

            System.out.println("Done!");
        }
        catch(Exception e)
        {
            System.err.println("Error: " + e);
        }
    }

    private void uploadScript(ChannelSftp sftpChannel) {
        if (isScriptExist(sftpChannel)) {
            return;
        }
        ClassLoader classLoader = SSHCleanBoiler.class.getClassLoader();
        URL resource = classLoader.getResource(CLEAN_BOILER_SCRIPT_PATH);
        if (Objects.isNull(resource)) {
            new RuntimeException("clean_boiler.sh script missed on the program, clean boiler function is not supported", new Throwable());
        }
        File script = new File(resource.getFile());
        try {
            sftpChannel.put(new FileInputStream(script), script.getName());
        } catch (SftpException | FileNotFoundException e) {
            e.printStackTrace();
        }
        System.out.println("Upload script " + script.getName() + " is SUCCESS");
    }

    private boolean isScriptExist(ChannelSftp sftpChannel) {
        boolean isExist = true;
        try {
            sftpChannel.lstat(CLEAN_BOILER_SCRIPT_NAME);
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                isExist = false;
            }
        }
        if (isExist) {
            System.out.println("File " + CLEAN_BOILER_SCRIPT_NAME + " exist!");
        } else {
            System.out.println("File " + CLEAN_BOILER_SCRIPT_NAME + " NOT exist!");
        }
        return isExist;
    }
}
