package com.server.service.function;

import com.client.view.controller.SessionFunctionController;
import com.jcraft.jsch.*;
import com.server.Constants;
import com.server.model.ssh.SSHManager;

import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

import static com.server.Constants.Server.*;

public class ScriptShExecutor {
    private Session session;
    private ChannelShell channel;
    private String username;
    private String password;
    private String hostname;
    private String commandName;
    private SessionFunctionController functionController;

    public ScriptShExecutor(SessionFunctionController functionController) {
        this.functionController = functionController;

        com.server.model.ssh.Session session = functionController.getSession();
        this.username = session.getUserName();
        this.password = session.getPassword();
        this.hostname = session.getHostName();
    }

    public void executeCommands(String commandName) {
        this.commandName = commandName;

        List<String> commands = new ArrayList<>();
        commands.add("cd " + SERVER_PATH);
        commands.add("sh " + commandName);
        try {
            Channel channel = getChannel();
            functionController.consoleAppendText("Sending commands...");
            sendCommands(channel, commands);
            readChannelOutput(channel);
            functionController.consoleAppendText("Finished sending " + commandName + "!");
        } catch (Exception e) {
            functionController.consoleAppendText("An error ocurred during executeCommands: " + e);
        }
        close();
    }

    private Session getSession() {
        if (session == null || !session.isConnected()) {
            session = connect(hostname, username, password);
        }
        return session;
    }

    private Channel getChannel() {
        if (channel == null || !channel.isConnected()) {
            try {
                channel = (ChannelShell) getSession().openChannel("shell");
                channel.connect();
            } catch (Exception e) {
                functionController.consoleAppendText("Error while opening channel: " + e);
            }
        }
        return channel;
    }

    private Session connect(String hostname, String username, String password) {
        JSch jSch = new JSch();
        try {
            session = jSch.getSession(username, hostname, Constants.Session.PORT);
            Properties config = new Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.setPassword(password);

            functionController.consoleAppendText("Connecting SSH to " + hostname + " - Please wait for few seconds... ");
            session.connect();
            functionController.consoleAppendText("Connected!");
        } catch (Exception e) {
            functionController.consoleAppendText("An error occurred while connecting to " + hostname + ": " + e );
        }
        return session;
    }

    private void sendCommands(Channel channel, List<String> commands) {
        try {
            PrintStream out = new PrintStream(channel.getOutputStream());

            out.println("#!/bin/bash");
            for (String command : commands) {
                out.println(command);
                functionController.consoleAppendText(command);
            }
            out.println("exit");
            functionController.consoleAppendText("exit");
            out.flush();
        } catch (Exception e) {
            functionController.consoleAppendText("Error while sending " + commandName + ": " + e);
        }
    }

    private void readChannelOutput(Channel channel) {
        byte[] buffer = new byte[1024];
        try {
            InputStream in = channel.getInputStream();
            String line = "";
            while (true) {
                while (in.available() > 0) {
                    int i = in.read(buffer, 0, 1024);
                    if (i < 0) {
                        break;
                    }
                    line = new String(buffer, 0, i);
                    functionController.consoleAppendText(line);
                }
                if (line.contains("logout")) {
                    break;
                }
                if (channel.isClosed()) {
                    break;
                }
                try {
                    Thread.sleep(1000);
                } catch (Exception ee) {
                    functionController.consoleAppendText(ee.getCause().toString());
                }
            }
        } catch (Exception e) {
            functionController.consoleAppendText("Error while reading channel output: " + e);
        }
    }

    public void uploadScriptIfNotExist(String scriptName, String scriptMissedMessage) {
        ChannelSftp sftpChannel;
        File script;
        try {
            sftpChannel = SSHManager.getInstance().getSFTPChannel(SERVER_PATH);
            if (isScriptExist(sftpChannel, scriptName)) {
                return;
            }
            ClassLoader classLoader = RestartServerFunction.class.getClassLoader();
            URL resource = classLoader.getResource("scripts/" + scriptName);
            if (Objects.isNull(resource)) {
                functionController.consoleAppendText(scriptName + " " + scriptMissedMessage);
                new RuntimeException(scriptName + " " + scriptMissedMessage, new Throwable());
            }
            script = new File(resource.getFile());
            sftpChannel.put(new FileInputStream(script), script.getName());
            functionController.consoleAppendText("File " + scriptName + " is success uploaded!");
        } catch (JSchException | SftpException | FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isScriptExist(ChannelSftp sftpChannel, String scriptName) {
        boolean isExist = true;
        try {
            sftpChannel.lstat(scriptName);
        } catch (SftpException e) {
            if (e.id == ChannelSftp.SSH_FX_NO_SUCH_FILE) {
                isExist = false;
            }
        }
        if (isExist) {
            functionController.consoleAppendText("File " + scriptName + " exist on the server.");
        } else {
            functionController.consoleAppendText("File " + scriptName + " is not exist on the server!");
        }
        return isExist;
    }

    private void close() {
        channel.disconnect();
        session.disconnect();
        functionController.consoleAppendText("Disconnected channel and session");
    }
}

