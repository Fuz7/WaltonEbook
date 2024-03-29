package org.openjfx.program;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.io.IOException;

@SuppressWarnings("CallToPrintStackTrace")
public class scene_loader {

    public static void loadScene(String fxmlPath, String title, Stage stage) {
        loadScene(fxmlPath, title, stage, -1, -1);
    }

    public static void loadScene(String fxmlPath, String title, Stage stage, double width, double height) {
        try {
            // Load the FXML file
            FXMLLoader fxmlLoader = new FXMLLoader(scene_loader.class.getResource(fxmlPath));
            Parent root = fxmlLoader.load();

            // Set up the scene
            Scene scene;
            if (width > 0 && height > 0) {
                scene = new Scene(root, width, height);
            } else {
                scene = new Scene(root);
            }

            // Set the stage with the new scene
            stage.setScene(scene);
            stage.setTitle(title);
            stage.show();

        } catch (IOException e) {
            e.printStackTrace(); // Handle the exception according to your needs
        }
    }

    public static void loadFonts(){
        Font.loadFont(app.class.getResourceAsStream("fonts/Karla-Bold.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/KottaOne-Regular.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/MergeOne-Regular.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/Merienda-Bold.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/KaiseiDecol-Regular.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/FugazOne-Regular.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/Karma-Bold.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/Koho-Bold.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/KaushanScript-Regular.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/Kanit-Bold.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/Buenard-Regular.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/Buenard-Bold.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/Karma-Regular.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/Poppins-Regular.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/GentiumBookPlus-Regular.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/GentiumBookPlus-Bold.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/Inter-Regular.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/Inter-Medium.ttf"), 16);
        Font.loadFont(app.class.getResourceAsStream("fonts/Inter-Bold.ttf"), 16);



    }

}
