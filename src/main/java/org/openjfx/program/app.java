package org.openjfx.program;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Screen;
import javafx.stage.Stage;
import org.openjfx.program.controller.homePageController;
import org.openjfx.program.controller.mainPageController;
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

/*        FXMLLoader fxmlLoader = new FXMLLoader(app.class.getResource("fxml/loginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());*/

        FXMLLoader fxmlLoader = new FXMLLoader(app.class.getResource("fxml/mainPage.fxml"));
        BorderPane root = fxmlLoader.load();
        mainPageController mainPageController = fxmlLoader.getController();
        mainPageController.replaceCenterPageContent(app.class.getResource("fxml/centerPages/homePage.fxml"), homePageController.class);
        Scene scene = new Scene(root);

        stage.setTitle("Hello!");
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