package com.client;

import com.MainApp;
import com.client.view.SessionEditController;
import com.client.view.SessionLayoutController;
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
            URL url = new URL("file:C:\\work\\Shelper\\src\\main\\java\\com\\client\\view\\rootLayout.fxml");
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
            URL url = new URL("file:C:\\work\\Shelper\\src\\main\\java\\com\\client\\view\\sessionsLayout.fxml");
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
            URL url = new URL("file:C:\\work\\Shelper\\src\\main\\java\\com\\client\\view\\sessionEditDialog.fxml");
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
            sessionEditController.setMainApp(mainApp);

            dialogStage.showAndWait();

            return sessionEditController.isOkClicked();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }
}
