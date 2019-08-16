package com.server.model.ssh;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;

public class SSHManager {

    private static final SSHManager INSTANCE = new SSHManager();
    private String user = "netcrk";
    private String password = "crknet";
    private String host = "10.109.1.195";
    private int port = 22;

    String remoteFile = "gsv";
    String absoluteServerPath = "/u02/netcracker/toms/u141_dev_6300";

    public static SSHManager getInstance() {
        return INSTANCE;
    }

    public ChannelSftp getSFTPChannel() throws JSchException {
        ChannelSftp sftpChannel = null;
        try {
            JSch jsch = new JSch();
            Session session = jsch.getSession(user, host, port);
            session.setPassword(password);
            session.setConfig("StrictHostKeyChecking", "no");
            System.out.println("Establishing Connection...");
            session.connect();
            System.out.println("Connection established.");
            System.out.println("Crating SFTP Channel.");
            sftpChannel = (ChannelSftp) session.openChannel("sftp");
            sftpChannel.connect(3000);
            System.out.println("SFTP Channel created.");
            sftpChannel.cd(absoluteServerPath);
        } catch (SftpException e) {
            e.printStackTrace();
        }
        return sftpChannel;
    }

    public void fetchSSHManager(com.server.model.ssh.Session session) {
        this.setHost(session.getHostName());
        this.setPort(session.getPortNumber());
        this.setUser(session.getUserName());
        this.setPassword(session.getPassword());
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }
}
