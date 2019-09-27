package com.client;

import com.MainApp;
import com.client.view.SessionEditController;
import com.client.view.SessionFunctionController;
import com.client.view.SessionLayoutController;
import com.client.view.SessionNewController;
import com.server.model.ssh.Session;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class ClientEntryPoint {

    private static String SESSION_NEW_DIALOG_PATH = "file:C:\\work\\Shelper\\src\\main\\java\\com\\client\\view\\sessionNewDialog.fxml";
    private static String SESSION_EDIT_DIALOG_PATH = "file:C:\\work\\Shelper\\src\\main\\java\\com\\client\\view\\sessionEditDialog.fxml";
    private static String ROOT_LAYOUT_DIALOG_PATH = "file:C:\\work\\Shelper\\src\\main\\java\\com\\client\\view\\rootLayout.fxml";
    private static String SESSION_LAYOUT_DIALOG_PATH = "file:C:\\work\\Shelper\\src\\main\\java\\com\\client\\view\\sessionsLayout.fxml";
    private static String SESSION_FUNCTION_DIALOG_PATH = "file:C:\\work\\Shelper\\src\\main\\java\\com\\client\\view\\sessionFunctionDialog.fxml";

    private static String TITLE = "Shelper";
    private static String EDIT_SESSION = "Edit Session";
    private static String NEW_SESSION = "New Session";
    private static String SESSION_FUNCTIONS = "Functions";

    private MainApp mainApp;
    private Stage primaryStage;
    private BorderPane rootLayout;

    public void startClientSide(MainApp mainApp, Stage primaryStage) {
        this.mainApp = mainApp;
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle(TITLE);
        this.primaryStage.getIcons().add(new Image("/icons/plus.png"));
        initRootLayout();
        initSessionsLayout();
    }

    private void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = new URL(ROOT_LAYOUT_DIALOG_PATH);
            loader.setLocation(url);
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.setResizable(false);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initSessionsLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            URL url = new URL(SESSION_LAYOUT_DIALOG_PATH);
            loader.setLocation(url);
            AnchorPane sessionOverview = loader.load();
            rootLayout.setCenter(sessionOverview);
            SessionLayoutController sessionLayoutController = loader.getController();
            sessionLayoutController.setMainApp(mainApp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean showSessionFunctionDialog(Session session) {
        FXMLLoader loader = new FXMLLoader();
        Stage dialogStage = initHBoxStage(loader, SESSION_FUNCTION_DIALOG_PATH, SESSION_FUNCTIONS);
        dialogStage.getIcons().add(new Image("/icons/plus.png"));

        SessionFunctionController sessionFunctionController = loader.getController();
        sessionFunctionController.setDialogStage(dialogStage);
        sessionFunctionController.setSession(session);

        dialogStage.showAndWait();

        return sessionFunctionController.isOkClicked();
    }

    public boolean showPersonEditDialog(Session session) {
        FXMLLoader loader = new FXMLLoader();
        Stage dialogStage = initAnchorStage(loader, SESSION_EDIT_DIALOG_PATH, EDIT_SESSION);
        dialogStage.getIcons().add(new Image("/icons/plus.png"));

        SessionEditController sessionEditController = loader.getController();
        sessionEditController.setDialogStage(dialogStage);
        sessionEditController.setSession(session);

        dialogStage.showAndWait();

        return sessionEditController.isOkClicked();
    }

    public boolean showPersonNewDialog(Session session) {
        FXMLLoader loader = new FXMLLoader();
        Stage dialogStage = initAnchorStage(loader, SESSION_NEW_DIALOG_PATH, NEW_SESSION);
        dialogStage.getIcons().add(new Image("/icons/plus.png"));

        SessionNewController sessionNewController = loader.getController();
        sessionNewController.setDialogStage(dialogStage);
        sessionNewController.setSession(session);

        dialogStage.showAndWait();

        return sessionNewController.isOkClicked();
    }

    private Stage initAnchorStage(FXMLLoader loader, String url, String dialogTitle) {
        AnchorPane page = getAnchorPaine(loader, url);
        return createStage(page, dialogTitle);
    }

    private Stage initHBoxStage(FXMLLoader loader, String url, String dialogTitle) {
        HBox page = getHBoxPaine(loader, url);
        return createStage(page, dialogTitle);
    }

    private Stage createStage(Pane page, String dialogTitle) {
        Stage dialogStage = new Stage();
        dialogStage.setTitle(dialogTitle);
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(primaryStage);
        Scene scene = new Scene(page);
        scene.getStylesheets().add("/style.css");
        dialogStage.setScene(scene);
        return dialogStage;
    }

    private AnchorPane getAnchorPaine(FXMLLoader loader, String urlPath) {
        return (AnchorPane) getPage(new AnchorPane(), loader, urlPath);
    }

    private HBox getHBoxPaine(FXMLLoader loader, String urlPath) {
        return (HBox) getPage(new HBox(), loader, urlPath);
    }

    private Pane getPage(Pane page, FXMLLoader loader, String urlPath) {
        try {
            URL url = new URL(urlPath);
            loader.setLocation(url);
            page = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return page;
    }
}
