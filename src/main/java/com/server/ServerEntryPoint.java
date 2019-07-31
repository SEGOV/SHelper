package com.server;

import com.server.service.SessionService;

public class ServerEntryPoint {
    private static final String FILE_PROTOCOL = "SFTP";
    private static final Integer PORT_NUMBER = 22;
    private static final String USER_NAME = "netcrk";
    private static final String USER_PASSWORD = "crknet";

    public void startServerSide() {
        SessionService sessionService = SessionService.getInstance();
        sessionService.createSessionTable();
//        sessionService.createSession(FILE_PROTOCOL, "10.109.1.195", PORT_NUMBER, USER_NAME, USER_PASSWORD);
//        sessionService.createSession(FILE_PROTOCOL, "10.109.3.206", PORT_NUMBER, USER_NAME, USER_PASSWORD);
    }
}
