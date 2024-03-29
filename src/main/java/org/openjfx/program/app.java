package org.openjfx.program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import org.openjfx.program.controller.homePageController;
import org.openjfx.program.controller.mainPageController;
import org.openjfx.program.database.DatabaseManager;
import org.openjfx.program.security.Login_manager;

import java.io.IOException;


public class app extends Application {
    static String location = "jdbc:sqlite:./src/main/java/org/openjfx/program/test.db";
    public static DatabaseManager db = new DatabaseManager(location);
    public static Login_manager lm = new Login_manager();

    @Override
    public void start(Stage stage) throws IOException {

        System.setProperty("prism.order", "sw");

        db.createTablesIfNotExist();
        lm.setSessionId(1);
        scene_loader.loadFonts();

        FXMLLoader fxmlLoader = new FXMLLoader(app.class.getResource("fxml/loginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());


        stage.setTitle("Login Page");
        // Center Screen
        stage.setX(0);
        stage.setY(0);

        stage.setScene(scene);
        stage.show();

    }   


    public static void main(String[] args) {
        launch();
    }
}