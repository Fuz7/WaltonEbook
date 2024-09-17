package org.openjfx.program.controller;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class aboutUsController implements Initializable {

    @FXML
    private HBox aboutUsPage__gojoContainer;
    @FXML
    private ImageView aboutUsPage__gojoImage;
    @FXML
    private Label aboutUsPage__founderBigTitle1;
    @FXML
    private Label aboutUsPage__founderBigTitle2;
    @FXML
    private  HBox aboutUsPage__iconicContainer;


    private boolean founderOfTheProjectAnimation = false;

    public mainPageController mainPageController;

    public void setMainPageController(mainPageController mainPageController) {
        this.mainPageController = mainPageController;
        this.mainPageController.hidePopups();
        aboutUsPage__gojoImage.setOpacity(0);
        aboutUsPage__founderBigTitle1.setOpacity(0);
        aboutUsPage__founderBigTitle2.setOpacity(0);
        aboutUsPage__iconicContainer.setOpacity(0);
        this.mainPageController.scrollContainer.vvalueProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {

                   if(newValue.doubleValue() > 0.4273185553483867 && !founderOfTheProjectAnimation){
                       aboutUsPage__gojoContainer.setVisible(true);
                       firstPartFounderAnimation();
                       founderOfTheProjectAnimation = true;
                   }
                });
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    private void firstPartFounderAnimation(){

        FadeTransition fadeGojo = new FadeTransition(Duration.millis(1000),aboutUsPage__gojoImage);
        fadeGojo.setFromValue(0);
        fadeGojo.setToValue(1);
        fadeGojo.setInterpolator(Interpolator.EASE_IN);
        FadeTransition killTimeAnimation1 = new FadeTransition(Duration.millis(600),aboutUsPage__gojoImage);
        FadeTransition killTimeAnimation2 = new FadeTransition(Duration.millis(800),aboutUsPage__gojoImage);
        FadeTransition killTimeAnimation3 = new FadeTransition(Duration.millis(1200),aboutUsPage__gojoImage);
        FadeTransition killTimeAnimation4 = new FadeTransition(Duration.millis(800),aboutUsPage__gojoImage);



        FadeTransition bigCeoAnimation = new FadeTransition(Duration.millis(500),aboutUsPage__founderBigTitle1);
        bigCeoAnimation.setFromValue(0);
        bigCeoAnimation.setToValue(1);
        bigCeoAnimation.setInterpolator(Interpolator.EASE_IN);

        FadeTransition bigModelAnimation = new FadeTransition(Duration.millis(500),aboutUsPage__founderBigTitle2);
        bigModelAnimation.setFromValue(0);
        bigModelAnimation.setToValue(1);
        bigModelAnimation.setInterpolator(Interpolator.EASE_IN);

        FadeTransition fadeOutContent1 = new FadeTransition(Duration.millis(800),aboutUsPage__gojoContainer);
        fadeOutContent1.setFromValue(1);
        fadeOutContent1.setToValue(0);
        fadeOutContent1.setInterpolator(Interpolator.EASE_OUT);

        FadeTransition fadeInContent2 = new FadeTransition(Duration.millis(300),aboutUsPage__iconicContainer);
        fadeInContent2.setFromValue(0);
        fadeInContent2.setToValue(1);
        fadeOutContent1.setInterpolator(Interpolator.EASE_IN);
        fadeGojo.setOnFinished(actionEvent -> {
            killTimeAnimation1.play();
        });
        killTimeAnimation1.setOnFinished(actionEvent -> {
            bigCeoAnimation.play();
        });

        bigCeoAnimation.setOnFinished(actionEvent -> {
            killTimeAnimation2.play();

        });
        killTimeAnimation2.setOnFinished(actionEvent -> {
            bigModelAnimation.play();
        });
        bigModelAnimation.setOnFinished(actionEvent -> {
            killTimeAnimation3.play();
        });
        killTimeAnimation3.setOnFinished(actionEvent -> {
            fadeOutContent1.play();
        });
        fadeOutContent1.setOnFinished(actionEvent -> {
           killTimeAnimation4.play();
        });
        killTimeAnimation4.setOnFinished(actionEvent -> {
            fadeInContent2.play();
        });

        fadeGojo.play();
    }
}
