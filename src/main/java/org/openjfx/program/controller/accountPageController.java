package org.openjfx.program.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.openjfx.program.app;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.*;
import java.util.Objects;

public class accountPageController {


    @FXML
    private ImageView detailsContainer__image;
    @FXML
    private TextField detailsContainer_balanceField;
    @FXML
    private Label detailsContainer__email;
    @FXML
    private Label detailsContainer__name;
    @FXML
    private Label detailsContainer__balance;

    public mainPageController mainPageController;

    public void setMainPageController(mainPageController mainPageController) {
        this.mainPageController = mainPageController;
        renderImageRoundedCorners();
        forceTextFieldToBeNumeric();
        setUserDetails();
        updateBalance();
    }

    private void renderImageRoundedCorners(){
        this.mainPageController.setRoundedImage(detailsContainer__image,String.valueOf(app.class.getResource("images/profile/defaultIcon.jpg")));
        BoxBlur boxBlur = new BoxBlur();
        boxBlur.setHeight(3);
        boxBlur.setWidth(3);
        boxBlur.setIterations(1);
        detailsContainer__image.setOnMouseEntered(mouseEvent -> {
            detailsContainer__image.setEffect(boxBlur);
        });
        detailsContainer__image.setOnMouseExited(mouseEvent -> {
            detailsContainer__image.setEffect(null);
        });
    }

    private void forceTextFieldToBeNumeric(){
        detailsContainer_balanceField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*")) {
                    detailsContainer_balanceField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    private  void setUserDetails(){
        int sessionId = app.lm.getSessionId();
        String email = app.db.Return.returnUserEmail(sessionId);
        String userName = app.db.Return.returnUserUserName(sessionId);
        detailsContainer__email.setText("Email: " + email);
        detailsContainer__name.setText("Name : " + userName);
    }

    private void updateBalance(){
        int sessionId = app.lm.getSessionId();
        double balance = app.db.Return.returnUserCash(sessionId);
        String formattedBalance = String.format("%.2f$",balance);
        detailsContainer__balance.setText("Balance: " + formattedBalance);
    }

    @FXML
    private void cashIn(){
        if(!Objects.equals(detailsContainer_balanceField.getText(), "")){
            int sessionId = app.lm.getSessionId();
            double oldBalance = app.db.Return.returnUserCash(sessionId);
            double inputBalance = Double.parseDouble(detailsContainer_balanceField.getText());
            double newBalance = oldBalance + inputBalance;
            app.db.Update.UpdateUserCash(sessionId,newBalance);
            detailsContainer_balanceField.setText("");
            updateBalance();
        }

    }

    @FXML
    private void renderCashInOnEnter(KeyEvent ev){
        if(ev.getCode() == KeyCode.ENTER) cashIn();
    }

    //This Doesnt work
    @FXML
    private void chooseFileProfile() throws URISyntaxException {

//        FileChooser fileChooser = new FileChooser();
//
//        // Set a filter to allow only image files
//        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif", "*.bmp");
//        fileChooser.getExtensionFilters().add(imageFilter);
//
//        Path to;
//        Path from;
//
//        // Show open file dialog
//        File selectedFile = fileChooser.showOpenDialog(null);
//
//        if (selectedFile != null) {
//            System.out.println("Selected Image: " + selectedFile.getAbsolutePath());
//            from = Paths.get(selectedFile.toURI());
//            to= Paths.get(app.class.getResource("images/profile/").toURI()).resolve(app.lm.getSessionId() +".jpg");
//
//            // Get the absolute path in string form
//            String absolutePathAsString = to.toAbsolutePath().toString();
//            try {
//                if(Files.exists(from)){
//                    CopyOption[] options = new CopyOption[]{
//                            StandardCopyOption.REPLACE_EXISTING,
//                    };
//                    Files.move(from,to,StandardCopyOption.REPLACE_EXISTING);
//                    System.out.print("file Copied to: " + absolutePathAsString);
//                }
//            } catch (IOException | SecurityException e) {
//                throw new RuntimeException(e);
//            }
//            // Add your logic for handling the selected image file
//        }
    }
}
