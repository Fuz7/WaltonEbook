package org.openjfx.program.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;
import org.openjfx.program.app;
import org.openjfx.program.model.BookData;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.animation.Interpolator.EASE_IN;

public class searchPageController implements Initializable {

    @FXML
    private StackPane searchPage__advanceSearchButton;
    @FXML
    private VBox searchPage__advanceSearchCard;
    @FXML
    private StackPane showOwnedButton__box;
    @FXML
    private GridPane advanceSearchCard__checkBoxContainer;
    @FXML
    private TextField searchPage__searchBar;
    @FXML
    private ToggleGroup searchBy;
    @FXML
    private FlowPane searchPage__bookLayout;

    private boolean animatingCard = false;


    public mainPageController mainPageController;

    public void setMainPageController(mainPageController mainPageController) {
        this.mainPageController = mainPageController;
        this.mainPageController.hidePopups();
        searchPage__advanceSearchCard.setManaged(false);
        searchPage__advanceSearchCard.setVisible(false);
        searchPage__advanceSearchCard.setMouseTransparent(true);
        renderSearchedBooks();
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

    private List<String> returnSelectedGenre(){
        List<String> selectedGenre = new ArrayList<>();
        advanceSearchCard__checkBoxContainer.getChildren().forEach(node ->{
            if(node instanceof CheckBox checkBox){
                if (checkBox.isSelected()){
                    selectedGenre.add(checkBox.getText());
                }
            }
        });
        return selectedGenre;
    }

    private String returnSearchByValue(){
        RadioButton selectedRadioButton = (RadioButton) searchBy.getSelectedToggle();
        return selectedRadioButton.getText();
    }
    private boolean returnShowOwnedState(){
        if(showOwnedButton__box.getStyleClass().contains("showOwnedButton__box")) return false;
        return showOwnedButton__box.getStyleClass().contains("showOwnedButton__box--visible");
    }

    @FXML
    private void renderSearchedBooksByFxml(){
        renderSearchedBooks();
    }

    private void renderSearchedBooks(){
        String searchQuery =  searchPage__searchBar.getText();
        List<Integer> bookIds = app.db.Return.returnSearch(returnSearchByValue(),returnSelectedGenre(),searchQuery,returnShowOwnedState(),app.lm.getSessionId());
        List<AnchorPane> bookCards = new ArrayList<AnchorPane>();
        searchPage__bookLayout.getChildren().clear();
        for (Integer bookId : bookIds) {
            bookCards.add(createBook(bookId));
        }
        for(AnchorPane bookCard: bookCards){
            searchPage__bookLayout.getChildren().add(bookCard);
        }

    }

    private AnchorPane createBook(int bookId){
        BookData bookData = mainPageController.getBookData(bookId);
        ImageView image = new ImageView();
        image.setImage(bookData.image);
        image.setFitHeight(233);
        image.setFitWidth(155);
        String title = bookData.title;
        Text titleElement = new Text();
        titleElement.getStyleClass().add("searchPage__bookContainer__title");
        titleElement.setWrappingWidth(197.13);
        titleElement.setTextAlignment(TextAlignment.CENTER);
        titleElement.setText(title);
        VBox imageAndTitleContainer = new VBox();
        VBox.setMargin(image, new javafx.geometry.Insets(20, 0, 0, 0));
        VBox.setMargin(titleElement, new javafx.geometry.Insets(18,0,0,0));
        imageAndTitleContainer.setMinWidth(246);
        imageAndTitleContainer.setMinHeight(315);
        imageAndTitleContainer.setAlignment(Pos.TOP_CENTER);
        imageAndTitleContainer.getChildren().addAll(image,titleElement);
        String priceTag = String.format("%.2f$",bookData.price);
        Label priceElement = new Label();
        AnchorPane searchBookContainer = new AnchorPane();
        searchBookContainer.setMaxHeight(355);
        searchBookContainer.setMaxWidth(246);
        priceElement.getStyleClass().add("searchPage__bookContainer__price");
        priceElement.setText(priceTag);
        AnchorPane.setBottomAnchor(imageAndTitleContainer,40.0);
        AnchorPane.setTopAnchor(priceElement,318.0);
        AnchorPane.setBottomAnchor(priceElement, 7.0);
        AnchorPane.setLeftAnchor(priceElement,179.0);
        AnchorPane.setRightAnchor(priceElement,0.0);
        searchBookContainer.setOnMouseClicked(mouseEvent -> {
            this.mainPageController.renderCheckBook(bookId);
        });
        searchBookContainer.getStyleClass().add("searchPage__bookContainer");
        searchBookContainer.getChildren().addAll(imageAndTitleContainer,priceElement);

        return searchBookContainer;
    }


    @FXML
    private void renderBooksOnEnter(KeyEvent ev){
        if(ev.getCode() == KeyCode.ENTER) renderSearchedBooks();
    }

    public void setSearchBarText(String text){
        searchPage__searchBar.setText(text);
    }
}
