package org.openjfx.demo;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;


public class app extends Application {
    @Override 
    public void start(Stage stage) throws IOException {
        String location = "jdbc:sqlite:./src/main/java/org/openjfx/demo/test.db";

        Database_manager db = new Database_manager();
        db.init(location);
        db.CreateTablesIfNoExist();

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