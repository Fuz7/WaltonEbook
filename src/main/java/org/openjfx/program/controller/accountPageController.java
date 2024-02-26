package org.openjfx.program.controller;

import javafx.fxml.FXML;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.ImageView;
import org.openjfx.program.app;

public class accountPageController {


    @FXML
    private ImageView detailsContainer__image;
    public mainPageController mainPageController;

    public void setMainPageController(mainPageController mainPageController) {
        this.mainPageController = mainPageController;
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



}
