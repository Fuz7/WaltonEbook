package org.openjfx.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class app extends Application {
    @Override 
    public void start(Stage stage) throws IOException {
        Font.loadFont(app.class.getResourceAsStream("fonts/Karla-Bold.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/KottaOne-Regular.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/MergeOne-Regular.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/Merienda-Bold.ttf"), 16);

        FXMLLoader fxmlLoader = new FXMLLoader(app.class.getResource("fxml/mainPage/mainPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        scene.getStylesheets().add(Objects.requireNonNull(app.class.getResource("css/navbar.css")).toExternalForm());
        stage.setTitle("Hello!");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}