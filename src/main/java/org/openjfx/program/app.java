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

        scene_loader.loadFonts();

/*        FXMLLoader fxmlLoader = new FXMLLoader(app.class.getResource("fxml/loginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());*/


        db.Insert.AdminBookInserter(
                "The Psychology of Money",
                "The Psychology of Money.jpeg",
                new String[]{"Finance", "Nonfiction", "Psychology", "Business", "Self Help", "Money", "Economics"},
                "Morgan Housel",
                true,
                5.99,
                39,
                "Doing well with money isn't necessarily about what you know. It's about how you behave. And behavior is hard to teach, even to really smart people. Money--investing, personal finance, and business decisions--is"
                        + " typically taught as a math-based field, where data and formulas tell us exactly what to do. But in the real world people don't make financial decisions on a spreadsheet. They make them at the dinner table, or "
                        + "in a meeting room, where personal history, your own unique view of the world, ego, pride, marketing, and odd incentives are scrambled together. In The Psychology of Money, award-winning author Morgan Housel shares "
                        + "19 short stories exploring the strange ways people think about money and teaches you how to make better sense of one of life's most important topics."
        );

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