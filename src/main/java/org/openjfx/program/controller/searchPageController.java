package org.openjfx.program.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class searchPageController implements Initializable {

    @FXML
    private VBox popup__myBooks;
    @FXML
    private VBox popup__account;

    public mainPageController mainPageController;

    public void setMainPageController(mainPageController mainPageController) {
        this.mainPageController = mainPageController;
        this.mainPageController.hidePopups();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    @FXML
    private void toggleNavbar__accountButton(){
        mainPageController.toggleNavbar__accountButton();
    }
    @FXML
    public void toggleNavbar__myBooksButton(){
        mainPageController.toggleNavbar__myBooksButton();
    }
}
