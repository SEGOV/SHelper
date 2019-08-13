package com;

import com.client.ClientEntryPoint;
import com.server.ServerEntryPoint;
import com.server.model.ssh.SSHCleanBoiler;
import com.server.model.ssh.Session;
import com.server.service.SessionService;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.stage.Stage;

import java.util.List;

public class MainApp extends Application {

    private Stage primaryStage;
    private ObservableList<Session> sessionData = FXCollections.observableArrayList();

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;

        new ServerEntryPoint().startServerSide();
        new ClientEntryPoint().startClientSide(this, primaryStage);
        new SSHCleanBoiler().executeCleanCommand();
    }

    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public ObservableList<Session> getSessionData() {
        List<Session> allSessions = SessionService.getInstance().getAllSessions();
        if (sessionData.isEmpty()) {
            sessionData.addAll(allSessions);
        }
        return sessionData;
    }
}
