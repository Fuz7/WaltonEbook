package org.openjfx.demo;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
}
