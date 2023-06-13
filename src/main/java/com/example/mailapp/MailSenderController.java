package com.example.mailapp;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

/**
 * This is an FXML controller class for the "Receive mail" window, mail-receiver-view.fxml.
 *
 * @author Evelina Bisell
 * @version 1.0
 */
public class MailSenderController {
    @FXML
    private TextField textFieldTo;
    @FXML
    private TextField textFieldSubject;
    @FXML
    private TextArea textArea;
    @FXML
    private TextField textFieldUsername;
    @FXML
    private TextField textFieldPassword;
    @FXML
    private Button buttonSend;
    @FXML
    private ToggleGroup radioButtons;
    @FXML
    private Label labelAttachments;

    private ArrayList<File> attachments = new ArrayList<>();

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
     * When the "Send" button is clicked, get info from text fields and radio buttons and call static method sendMail()
     * in the MailSender class with it. Then show an alert saying if the e-mail seems to have been sent or not. If
     * it has, clear text fields and areas.
     */
    @FXML
    protected void onSendButtonClick() {
        RadioButton selectedRadioBtn = (RadioButton) radioButtons.getSelectedToggle();
        String encryptionType = selectedRadioBtn.getId();

        buttonSend.setDisable(true);

        boolean sent = MailSender.sendMail(encryptionType, textFieldTo.getText(), textFieldSubject.getText(),
                textArea.getText(), textFieldUsername.getText(), textFieldPassword.getText(), attachments);

        // Empty text fields and text area if the e-mail could be sent
        if (sent) {
            textFieldTo.setText("");
            textFieldSubject.setText("");
            textArea.setText("");
            labelAttachments.setText("");
            attachments.clear();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Message sent");
            alert.setHeaderText(null);
            alert.setGraphic(null);
            alert.show();

        } else {
            new Alert(Alert.AlertType.ERROR, "Message could not be sent. Check that your username and password " +
                    "is correct").show();
        }
        buttonSend.setDisable(false);
    }

    /**
     * When "Add attachment" is clicked, open a file chooser that lets the user choose a file which is saved to a list
     * of files, and every file in the list is shown by a label
     */
    @FXML
    protected void onAddAttachmentButtonClick() {
        FileChooser fileChooser = new FileChooser();
        attachments.add(fileChooser.showOpenDialog(buttonSend.getScene().getWindow()));

        StringBuilder fileNames = new StringBuilder();
        for (File file : attachments) {
            fileNames.append(file.getName()).append(", ");
        }
        fileNames.setLength(fileNames.length() - 2);

        labelAttachments.setText(fileNames.toString());
    }
}