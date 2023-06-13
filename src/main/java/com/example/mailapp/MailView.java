package com.example.mailapp;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebView;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * A custom component to display mail content and attachments that inherits from JavaFX BorderPane.
 *
 * @author Evelina Bisell
 * @version 1.0
 */
public class MailView extends BorderPane {
    private final WebView webView;

    /**
     * Constructs a MailView object, which is a BorderPane with a WebView added to the center of it.
     */
    public MailView() {
        webView = new WebView();
        setCenter(webView);
    }

    /**
     * Add a String with plain text or HTML content to the WebView in the center of this object.
     *
     * @param content the text (plain or HTML or other) to show put in this object's webview
     */
    public void loadContent(String content) {
        webView.getEngine().loadContent(content);
    }

    /**
     * Add ListView with filenames of attachments to bottom area of this MailView and open the file on select.
     *
     * @param attachments list of Files to add to listView
     */
    public void loadAttachments(ArrayList<File> attachments) {
        // Make a listView with the names of the attached files
        ListView<String> listView = new ListView<>();
        ObservableList<String> items = FXCollections.observableArrayList();
        for (File attachment : attachments) {
            items.add(attachment.getName());
        }
        listView.setItems(items);

        // On select open the file (which has already been downloaded to the computer)
        listView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            for (File attachment : attachments) {
                if (attachment.getName().equals(newValue)) {
                    try {
                        Desktop.getDesktop().open(attachment);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        // Set maximum height of listView and add it (and a label) to a vertical box container
        listView.setMaxHeight(50);
        VBox vBox = new VBox();
        vBox.setSpacing(5);
        vBox.getChildren().add(new Label("Attachments:"));
        vBox.getChildren().add(listView);
        // Add the vertical box container to the bottom of the window
        setBottom(vBox);
    }
}