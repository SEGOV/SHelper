package com.server.model.ssh;

import java.util.Objects;

public class LogInfo {
    private Integer id;
    private String login;
    private String password;

    public LogInfo() {
    }

    public LogInfo(Integer id, String login, String password) {
        this.id = id;
        this.login = login;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
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
        LogInfo logInfo = (LogInfo) o;
        return Objects.equals(id, logInfo.id) &&
                Objects.equals(login, logInfo.login) &&
                Objects.equals(password, logInfo.password);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, login, password);
    }
}
