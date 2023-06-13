package com.example.mailapp;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * This is an FXML controller class for the log in window, log-in-view.fxml.
 *
 * @author Evelina Bisell
 * @version 1.0
 */
public class LogInController {
    @FXML
    private TextField textFieldUsername;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private Label labelLoggedIn;

    /**
     * When the login button is clicked, create a user object with login info and add it to the stage.
     */
    @FXML
    protected void onLogInClick() {
        User user = new User(textFieldUsername.getText(), textFieldPassword.getText());

        Stage stage = (Stage) textFieldUsername.getScene().getWindow();
        stage.setUserData(user);

        labelLoggedIn.setText("Login information saved!");
    }
}
