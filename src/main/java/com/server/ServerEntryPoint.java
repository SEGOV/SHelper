package com.server;

import com.server.service.SessionService;

public class ServerEntryPoint {
    public void startServerSide() {
        SessionService sessionService = SessionService.getInstance();
        sessionService.createSessionTable();
    }
}
