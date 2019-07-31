package com.server.model.ssh;

import java.util.Objects;

public class Session {
    private Integer id;
    private String fileProtocol;
    private String hostName;
    private int portNumber;
    private String userName;
    private String password;

    public Session() {
    }

    public Session(Integer id, String fileProtocol, String hostName, int portNumber, String userName, String password) {
        this.id = id;
        this.fileProtocol = fileProtocol;
        this.hostName = hostName;
        this.portNumber = portNumber;
        this.userName = userName;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFileProtocol() {
        return fileProtocol;
    }

    public void setFileProtocol(String fileProtocol) {
        this.fileProtocol = fileProtocol;
    }

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
    }

    public int getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(int portNumber) {
        this.portNumber = portNumber;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Session session = (Session) o;
        return portNumber == session.portNumber &&
                Objects.equals(id, session.id) &&
                Objects.equals(fileProtocol, session.fileProtocol) &&
                Objects.equals(hostName, session.hostName) &&
                Objects.equals(userName, session.userName) &&
                Objects.equals(password, session.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, fileProtocol, hostName, portNumber, userName, password);
    }
}
