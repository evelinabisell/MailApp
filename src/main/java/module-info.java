module com.example.mailapp {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.graphics;
    requires java.mail;
    requires java.desktop;
    requires activation;


    opens com.example.mailapp to javafx.fxml;
    exports com.example.mailapp;
}