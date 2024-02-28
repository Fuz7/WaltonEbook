package org.openjfx.program.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import org.openjfx.program.app;

import java.io.*;
import java.net.MalformedURLException;
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

    public void setMainPageController(mainPageController mainPageController) throws MalformedURLException {
        this.mainPageController = mainPageController;
        renderImageRoundedCorners();
        forceTextFieldToBeNumeric();
        setUserDetails();
        updateBalance();
    }

    private void renderImageRoundedCorners() throws MalformedURLException {
        this.mainPageController.setAccountRoundedImage(detailsContainer__image,this.mainPageController.returnAccountImage());
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

        FileChooser fileChooser = new FileChooser();

        // Set a filter to allow only image files
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);

        Path to;
        Path from;

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            System.out.println("Selected Image: " + selectedFile.getAbsolutePath());
            from = Paths.get(selectedFile.toURI());

            String extension = "jpg";


            Path sourceDirectory = Paths.get("src/main/resources/org/openjfx/program/images/profile/");

            to= sourceDirectory.resolve(app.lm.getSessionId() + "." + extension);

            try {
                if(Files.exists(from)){
                    copyFileUsingStream(from.toFile(),to.toFile());
                    System.out.print("file Copied;");
                    renderImageRoundedCorners();
                    this.mainPageController.setNavbarRoundedImage(this.mainPageController.profileLogo,this.mainPageController.returnAccountImage());
                }
            } catch (IOException | SecurityException e) {
                throw new RuntimeException(e);
            }
            // Add your logic for handling the selected image file
        }
    }

    private void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;


        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            assert is != null;
            is.close();
            assert os != null;
            os.close();
        }
    }

}

