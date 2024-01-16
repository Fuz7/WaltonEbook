package org.openjfx.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class app extends Application {
    @Override 
    public void start(Stage stage) throws IOException {

        scene_loader.loadFonts();

        FXMLLoader fxmlLoader = new FXMLLoader(app.class.getResource("fxml/mainPage/mainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}