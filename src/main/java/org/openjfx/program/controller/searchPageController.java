package org.openjfx.program.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

import static javafx.animation.Interpolator.EASE_IN;

public class searchPageController implements Initializable {

    @FXML
    private StackPane searchPage__advanceSearchButton;
    @FXML
    private VBox searchPage__advanceSearchCard;
    @FXML
    private StackPane showOwnedButton__box;

    private boolean animatingCard = false;


    public mainPageController mainPageController;

    public void setMainPageController(mainPageController mainPageController) {
        this.mainPageController = mainPageController;
        this.mainPageController.hidePopups();
        searchPage__advanceSearchCard.setManaged(false);
        searchPage__advanceSearchCard.setVisible(false);
        searchPage__advanceSearchCard.setMouseTransparent(true);
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

    @FXML
    private void animateAdvanceSearchCard(){
        if(!animatingCard&&!searchPage__advanceSearchCard.isVisible()){
            dropAnimateAdvanceSearchCard();
        } else if (!animatingCard&& searchPage__advanceSearchCard.isVisible()){
            returnAnimateAdvanceSearchCard();
        }

    }


    private void dropAnimateAdvanceSearchCard(){
        animatingCard = true;
        searchPage__advanceSearchCard.setVisible(true);
        searchPage__advanceSearchCard.setManaged(true);
        TranslateTransition translateAnimation = new TranslateTransition(Duration.millis(300),searchPage__advanceSearchCard);
        FadeTransition fadeInAnimation = new FadeTransition(Duration.millis(300),searchPage__advanceSearchCard);
        fadeInAnimation.setFromValue(0);
        fadeInAnimation.setToValue(1);
        fadeInAnimation.setInterpolator(EASE_IN);
        translateAnimation.setFromY(-100f);
        translateAnimation.setToY(0f);
        translateAnimation.setInterpolator(EASE_IN);

        translateAnimation.setOnFinished(actionEvent -> {
            searchPage__advanceSearchCard.setMouseTransparent(false);
            animatingCard = false;
        });
        fadeInAnimation.play();
        translateAnimation.play();
    }


    private void returnAnimateAdvanceSearchCard(){
        animatingCard = true;
        searchPage__advanceSearchCard.setMouseTransparent(true);

        TranslateTransition translateAnimation = new TranslateTransition(Duration.millis(300),searchPage__advanceSearchCard);
        FadeTransition fadeInAnimation = new FadeTransition(Duration.millis(300),searchPage__advanceSearchCard);
        fadeInAnimation.setFromValue(1);
        fadeInAnimation.setToValue(0);
        fadeInAnimation.setInterpolator(EASE_IN);
        translateAnimation.setFromY(0f);
        translateAnimation.setToY(-100f);
        translateAnimation.setInterpolator(EASE_IN);
        translateAnimation.setOnFinished(actionEvent -> {
            searchPage__advanceSearchCard.setVisible(false);
            searchPage__advanceSearchCard.setManaged(false);
            animatingCard = false;
        });
        fadeInAnimation.play();
        translateAnimation.play();
    }

    @FXML
    private void toggleShowOwnedButton(){
        if(showOwnedButton__box.getStyleClass().contains("showOwnedButton__box")){
            showOwnedButton__box.getStyleClass().remove("showOwnedButton__box");
            showOwnedButton__box.getStyleClass().add("showOwnedButton__box--visible");
        }else if(showOwnedButton__box.getStyleClass().contains("showOwnedButton__box--visible")){
            showOwnedButton__box.getStyleClass().remove("showOwnedButton__box--visible");
            showOwnedButton__box.getStyleClass().add("showOwnedButton__box");
        }
    }

}
