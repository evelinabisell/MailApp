package com.example.mailapp;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * This is an FXML controller class for the main application window, main-view.fxml.
 * It contains methods to load and set the appropriate FXML views and controllers when buttons are clicked.
 *
 * @author Evelina Bisell
 * @version 1.0
 */
public class MainController {
    @FXML
    private BorderPane mainPane;

    /**
     * Called when the "Send mail" button is clicked.
     * The method loads the mail-sender-view FXML file and sets it as the center pane of the mainPane BorderPane.
     * It also retrieves the controller for the mail-sender-view and calls its setLoginInfo() method to fetch and
     * display login info from the stage.
     *
     * @throws RuntimeException if an error occurs while loading the FXML file
     */
    @FXML
    protected void onSendMailButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mail-sender-view.fxml"));
            mainPane.setCenter(fxmlLoader.load());

            Stage stage = (Stage) mainPane.getScene().getWindow();
            MailSenderController controller = fxmlLoader.getController();
            controller.setLoginInfo(stage);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Called when the "Receive mail" button is clicked.
     * The method loads the mail-receiver-view FXML file and sets it as the center pane of the mainPane BorderPane.
     * It also retrieves the controller for the mail-receiver-view and calls its setLoginInfo() method to fetch and
     * display login info from the stage.
     *
     * @throws RuntimeException if an error occurs while loading the FXML file
     */
    @FXML
    protected void onReceiveMailButtonClick() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("mail-receiver-view.fxml"));
            mainPane.setCenter(fxmlLoader.load());

            Stage stage = (Stage) mainPane.getScene().getWindow();
            MailReceiverController controller = fxmlLoader.getController();
            controller.setLoginInfo(stage);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Called when the "Log in" button is clicked.
     * The method loads the log-in-view FXML file and sets it as the center pane of the mainPane BorderPane.
     *
     * @throws RuntimeException if an error occurs while loading the FXML file
     */
    @FXML
    protected void onLogInButtonClick() {
        try {
            mainPane.setCenter(new FXMLLoader().load(getClass().getResource("log-in-view.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
