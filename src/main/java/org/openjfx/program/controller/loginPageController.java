package org.openjfx.program.controller;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.openjfx.program.AdminPanelCls;
import org.openjfx.program.app;
import org.openjfx.program.pdfCreator;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class loginPageController implements Initializable {
    @FXML
    private TextField login__emailInput;

    @FXML
    private TextField login__passwordInput;
    @FXML
    private Label login__errorText;
    @FXML
    private VBox login__adminContainer;
    @FXML
    private Button login__noButton;

    public void initialize(URL locations, ResourceBundle resources) {
        login__errorText.setVisible(false);
        login__emailInput.setText("");
        login__passwordInput.setText("");
        login__emailInput.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                loginButtonOnclickHandler();
            }else{
                loginInputsOnChangeHandler();
            }
        });
        login__passwordInput.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                loginButtonOnclickHandler();
            }else{
                loginInputsOnChangeHandler();
            }
        });

        login__noButton.setOnMouseClicked(event -> {
            try {
                switchToMainScene();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        login__adminContainer.setVisible(false);
        login__adminContainer.setMouseTransparent(true);
    }
    @FXML
    private void closeAndLaunch() {

        // Schedule the new application logic to run after the JavaFX platform exits
        Platform.runLater(() -> {
            try {
                AdminPanelCls.main(new String[]{});
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // Exit the JavaFX platform
        Platform.exit();
    }

    public void loginInputsOnChangeHandler(){
        login__errorText.setVisible(false);
        login__emailInput.getStyleClass().remove("login__emailInput--error");
        login__passwordInput.getStyleClass().remove("login__passwordInput--error");
    }

    @FXML
    public void loginButtonOnclickHandler() {
        String email = login__emailInput.getText();
        String password = login__passwordInput.getText();

        try {
            int userId = app.db.Login.ReturnUserIdByLogIn(email, password);

            if (userId > 0) {
                // Insert data in login manager
                app.lm.setSessionId(userId);
                System.out.println("Login successful.");

                // SWITCH SCENE
                if(app.db.Check.checkIfAdmin(app.lm.getSessionId())){
                    login__adminContainer.setVisible(true);
                    login__adminContainer.setMouseTransparent(false);
                }else{
                    switchToMainScene();

                }
                System.out.println(app.lm.getSessionId());

            } else {
                // DO CSS STUFF ERE
                login__errorText.setVisible(true);
                login__emailInput.getStyleClass().add("login__emailInput--error");
                login__passwordInput.getStyleClass().add("login__passwordInput--error");
            }

        } catch (Exception e) {
            // Handle other exceptions
            System.err.println("Unexpected error during login: " + e.getMessage());
        }
    }

    private void switchToMainScene() throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(app.class.getResource("fxml/mainPage.fxml"));
        BorderPane root = fxmlLoader.load();
        mainPageController mainPageController = fxmlLoader.getController();
        mainPageController.replaceCenterPageContent(app.class.getResource("fxml/centerPages/homePage.fxml"),homePageController.class);

        Stage currentStage = (Stage) login__emailInput.getScene().getWindow();
        Scene scene = new Scene(root);
        currentStage.setScene(scene);
        currentStage.setTitle("Main Page");


        currentStage.show();
    }

    @FXML
    private void switchToCreateAccountScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(app.class.getResource("fxml/createAccountPage.fxml"));
        Scene createAccountScene = new Scene(fxmlLoader.load());

        Stage currentStage = (Stage) login__emailInput.getScene().getWindow();
        currentStage.setScene(createAccountScene);
        currentStage.setTitle("Create Account");


        currentStage.show();
    }
}
