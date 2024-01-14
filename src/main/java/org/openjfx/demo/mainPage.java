package org.openjfx.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class mainPage extends Application {
    @Override 
    public void start(Stage stage) throws IOException {
        Font.loadFont(mainPage.class.getResourceAsStream("fonts/Karla-Bold.ttf"), 16);

        FXMLLoader fxmlLoader = new FXMLLoader(mainPage.class.getResource("fxml/mainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1800, 1000);
        scene.getStylesheets().add(Objects.requireNonNull(mainPage.class.getResource("css/navbar.css")).toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}