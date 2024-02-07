package org.openjfx.program.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.openjfx.program.app;

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



    public void initialize(URL locations, ResourceBundle resources) {
        login__errorText.setVisible(false);
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
            int userId = app.db.ReturnUserIdByLogIn(email, password);

            if (userId > 0) {
                // Insert data in login manager
                app.lm.setSessionId(userId);
                System.out.println("Login successful.");

                // SWITCH SCENE
                switchToMainScene();
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
        FXMLLoader fxmlLoader = new FXMLLoader(app.class.getResource("fxml/mainPages/mainPage__homePage.fxml"));
        Scene mainScene = new Scene(fxmlLoader.load());

        Stage currentStage = (Stage) login__emailInput.getScene().getWindow();
        currentStage.setScene(mainScene);
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
