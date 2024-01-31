package org.openjfx.program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.openjfx.program.security.Login_manager;

import java.io.IOException;


public class app extends Application {
    public static Database_manager db = new Database_manager();
    public static Login_manager lm = new Login_manager();

    @Override
    public void start(Stage stage) throws IOException {
        String location = "jdbc:sqlite:./src/main/java/org/openjfx/program/test.db";

        System.setProperty("prism.order", "sw");

        db.init(location);
        db.CreateTablesIfNoExist();

        scene_loader.loadFonts();

        FXMLLoader fxmlLoader = new FXMLLoader(app.class.getResource("fxml/loginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Hello!");
        // Center Screen
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX((screenBounds.getWidth() - stage.getWidth()) / 2);
        stage.setY(0);

        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch();
    }
}