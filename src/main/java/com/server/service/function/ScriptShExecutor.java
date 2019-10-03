package com.server.service.function;

import com.client.view.controller.SessionFunctionController;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelShell;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.server.Constants;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.List;
import java.util.Properties;

public class ScriptShExecutor {
    private Session session;
    private ChannelShell channel;
    private String username;
    private String password;
    private String hostname;
    private String stringCommandName;
    private SessionFunctionController functionController;

    public ScriptShExecutor(SessionFunctionController functionController) {
        this.functionController = functionController;

        com.server.model.ssh.Session session = functionController.getSession();
        this.username = session.getUserName();
        this.password = session.getPassword();
        this.hostname = session.getHostName();
    }

    public void executeCommands(String stringCommandName, List<String> commands) {
        this.stringCommandName = stringCommandName;
        try {
            Channel channel = getChannel();
            functionController.consoleAppendText("Sending commands...");
            sendCommands(channel, commands);
            readChannelOutput(channel);
            functionController.consoleAppendText("Finished sending " + stringCommandName + "!");
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
            functionController.consoleAppendText("Error while sending " + stringCommandName + ": " + e);
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

    private void close() {
        channel.disconnect();
        session.disconnect();
        functionController.consoleAppendText("Disconnected channel and session");
    }
}

