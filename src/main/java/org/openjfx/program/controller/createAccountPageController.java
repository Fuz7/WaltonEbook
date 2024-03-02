package org.openjfx.program.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import org.openjfx.program.app;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class createAccountPageController implements Initializable {





    @FXML
    private TextField createEmail;

    @FXML
    private TextField createName;

    @FXML
    private TextField createPassword;

    @FXML
    private Label createAccount__errorText;

    @FXML
    private void CreateNewAccount() throws IOException {
        String email = createEmail.getText();
        String name = createName.getText();
        String password = createPassword.getText();


        // Email regex pattern for basic validation
        String emailRegex = "\\b[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Z|a-z]{2,}\\b";

        // Check if the email matches the regex pattern
        if (!email.matches(emailRegex)) {
            // ADD CSS MAGIC HERE
            showError("Invalid Email Address");
            createEmail.getStyleClass().add("emailInput--error");
            return;
        }



        // Check if email already exist
        if (app.db.Check.CheckIfEmailAlreadyExist(email)){
            // ADD CSS MAGIC HERE
            showError("Email Already Exist");
            createEmail.getStyleClass().add("emailInput--error");
            return;
        }


        if(name.isEmpty()){
            showError("Empty Name Input");
            createName.getStyleClass().add("userInput--error");
            return;
        }

        // Check if username already exist
        if (app.db.Check.CheckIfUserNameAlreadyExist(name)) {
            // ADD CSS MAGIC HERE
            showError("Username Already Exist");
            createName.getStyleClass().add("userInput--error");
            return;
        }

        if(password.length() < 8){
            showError("Password Is Too Short");
            createPassword.getStyleClass().add("passwordInput--error");
            return;
        }

        // Register the account (ADJUST DEFAULT BALANCE {SUBJECT TO CHANGE})
        app.db.Insert.InsertNewUser(email, name, password, false, 0);
        app.db.Insert.insertAuthor(app.db.Return.returnLatestUserId(),"");
        clearText();
        // Switch
        switchToLogInScene();

    }

    private void clearText(){
        createPassword.clear();
        createName.clear();
        createEmail.clear();
    }


    private void showError(String errorText){
        createAccount__errorText.setText(errorText);
        createAccount__errorText.setVisible(true);
    }

    // use switch scene logic here
    @FXML
    private void switchToLogInScene() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(app.class.getResource("fxml/LoginPage.fxml"));
        Scene createAccountScene = new Scene(fxmlLoader.load());

        Stage currentStage = (Stage) createEmail.getScene().getWindow();
        currentStage.setScene(createAccountScene);
        currentStage.setTitle("Create Account");


        currentStage.show();
    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createAccount__errorText.setVisible(false);
        createEmail.setText("");
        createPassword.setText("");
        createName.setText("");

        createEmail.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                try {
                    CreateNewAccount();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                createEmail.getStyleClass().remove("emailInput--error");
            }
        });
        createName.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                try {
                    CreateNewAccount();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                createName.getStyleClass().remove("userInput--error");
            }
        });
        createPassword.setOnKeyPressed(ev -> {
            if (ev.getCode() == KeyCode.ENTER) {
                try {
                    CreateNewAccount();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }else{
                createPassword.getStyleClass().remove("passwordInput--error");
            }
        });
    }
}