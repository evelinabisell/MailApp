package com.example.mailapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.util.ArrayList;

/**
 * This is an FXML controller class for the "Receive mail" window, mail-receiver-view.fxml.
 *
 * @author Evelina Bisell
 * @version 1.0
 */
public class MailReceiverController {
    @FXML
    private TextField textFieldUsername;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private ListView<Mail> listView;
    @FXML
    private Slider sliderNumber;

    /**
     * Check if username and password has been saved in a user object and set to the stage,
     * if so put the information in the username and password textFields.
     *
     * @param stage the current top level JavaFX container that holds a User object
     */
    @FXML
    public void setLoginInfo(Stage stage) {
        User user = (User) stage.getUserData();
        if (user != null) {
            textFieldUsername.setText(user.getUsername());
            textFieldPassword.setText(user.getPassword());
        }
    }

    /**
     * When "Receive" button is clicked, receive a list of mails from MailReceiver instance and add them to a listview
     */
    @FXML
    protected void onReceiveButtonClick() {
        MailReceiver mailReceiver = new MailReceiver(textFieldUsername.getText(), textFieldPassword.getText(),
                                                    (int)sliderNumber.getValue());
        ArrayList<Mail> mails = mailReceiver.receiveMails();

        // Add mails to listview and if one is clicked, display the content of the mail
        ObservableList<Mail> observableList = FXCollections.observableArrayList(mails);
        listView.setItems(observableList);
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            for (Mail mail : mails) {
                if (mail.getId().equals(newValue.getId())) {
                    displayMailContent(mail);
                }
            }
        });
    }

    // Display e-mail content and possible attachments in a separate window with custom borderpane MailView
    private void displayMailContent(Mail mail) {
        MailView emailView = new MailView();
        emailView.loadContent(mail.getContent());

        if (!mail.getAttachments().isEmpty()) {
            emailView.loadAttachments(mail.getAttachments());
        }

        Stage stage = new Stage();
        Scene scene = new Scene(emailView, 800, 600);
        stage.setScene(scene);
        stage.show();
    }
}