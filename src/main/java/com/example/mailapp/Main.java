package com.example.mailapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The Main class starts up the JavaFX GUI and shows the main-view.
 *
 * @author Evelina Bisell
 * @version 1.0
 */
public class Main extends Application {

    /**
     * Starts the JavaFX GUI and shows the main-view.
     *
     * @param stage the primary stage for the application
     * @throws IOException if an error occurs while loading the FXML file
     */
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("main-view.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load(), 720, 400);

        stage.setTitle("Mail App");
        stage.setScene(mainScene);
        stage.show();
    }

    /**
     * Calls the launch() method to start the application and show the GUI.
     *
     * @param args command-line arguments, which are not used
     */
    public static void main(String[] args) {
        launch();
    }
}