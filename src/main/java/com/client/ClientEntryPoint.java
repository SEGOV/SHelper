package com.client;

import com.MainApp;
import com.client.view.SessionEditController;
import com.client.view.SessionLayoutController;
import com.client.view.SessionNewController;
import com.server.model.ssh.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ClientEntryPoint {

    public static String SESSION_NEW_DIALOG_PATH = "file:C:\\work\\Shelper\\src\\main\\java\\com\\client\\view\\sessionNewDialog.fxml";
    public static String SESSION_EDIT_DIALOG_PATH = "file:C:\\work\\Shelper\\src\\main\\java\\com\\client\\view\\sessionEditDialog.fxml";
    public static String ROOT_LAYOUT_DIALOG_PATH = "file:C:\\work\\Shelper\\src\\main\\java\\com\\client\\view\\rootLayout.fxml";
    public static String SESSION_LAYOUT_DIALOG_PATH = "file:C:\\work\\Shelper\\src\\main\\java\\com\\client\\view\\sessionsLayout.fxml";
    private MainApp mainApp;
    private Stage primaryStage;
    private BorderPane rootLayout;

    public void startClientSide(MainApp mainApp, Stage primaryStage) {
        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("Shelper");
        initRootLayout();
        initSessionsLayout();
    }

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = new URL(ROOT_LAYOUT_DIALOG_PATH);
            loader.setLocation(url);
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initSessionsLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = new URL(SESSION_LAYOUT_DIALOG_PATH);
            loader.setLocation(url);
            AnchorPane sessionOverview = loader.load();

            // Set sessionsLayout into center of the border rootLayout.
            rootLayout.setCenter(sessionOverview);
            SessionLayoutController sessionLayoutController = loader.getController();
            sessionLayoutController.setMainApp(mainApp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showPersonEditDialog(Session session) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = new URL(SESSION_EDIT_DIALOG_PATH);
            loader.setLocation(url);
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Edit Session");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            SessionEditController sessionEditController = loader.getController();
            sessionEditController.setDialogStage(dialogStage);
            sessionEditController.setSession(session);

            dialogStage.showAndWait();

            return sessionEditController.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean showPersonNewDialog(Session session) {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = new URL(SESSION_NEW_DIALOG_PATH);
            loader.setLocation(url);
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("New Session");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(primaryStage);
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            SessionNewController sessionNewController = loader.getController();
            sessionNewController.setDialogStage(dialogStage);
            sessionNewController.setSession(session);

            dialogStage.showAndWait();

            return sessionNewController.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
