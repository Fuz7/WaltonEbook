package org.openjfx.program.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.openjfx.program.app;
import org.openjfx.program.model.BookData;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import static javafx.animation.Interpolator.EASE_IN;


@SuppressWarnings("CallToPrintStackTrace")
public class mainPageController implements Initializable {

    @FXML
    private VBox popup__myBooks;
    @FXML
    private VBox popup__account;
    @FXML
    private Button navbar__myBookButton;
    @FXML
    private ImageView navbar__myBooks__arrow;
    @FXML
    private ScrollPane scrollContainer;
    @FXML
    private ImageView checkBook__bookImage;
    @FXML
    private StackPane popup__checkBook;
    @FXML
    private StackPane checkBook__closeButton;
    @FXML
    private TextField navbar__searchBar;

    private boolean myBooks__animating = false;
    private boolean account__animating = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hidePopups();
    }

    public void hidePopups(){
        popup__myBooks.setMouseTransparent(true);
        popup__myBooks.setVisible(false);
        popup__account.setMouseTransparent(true);
        popup__account.setVisible(false);
        popup__checkBook.setMouseTransparent(true);
        popup__checkBook.setVisible(false);
    }

    public BookData getBookData(int book_id){
        String[] value = app.db.Return.returnBookDataById(book_id);
        String title = (String) value[1];
        String description = (String) value[2];
        String formattedDescription = description.replaceAll("\\n", " ");
        String imageLink = (String) value[3];
        String correctPath = String.valueOf(app.class.getResource("images/books/" + imageLink));

        Image image = new Image(correctPath);
        String genre = "Tags: " + value[4];
        int bookSold = Integer.parseInt(value[8]);
        double price = Double.parseDouble(value[7]);
        return new BookData(title, formattedDescription,image,genre, bookSold,price);
    }
    @FXML
    public void toggleNavbar__accountButton(){
        if(!account__animating && !popup__account.isVisible()){
            account__animating = true;
            popup__account.setVisible(true);
            TranslateTransition translateAnimation = new TranslateTransition(Duration.millis(150),popup__account);
            FadeTransition fadeIn = new FadeTransition(Duration.millis(150),popup__account);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);
            fadeIn.setInterpolator(EASE_IN);
            translateAnimation.setFromY(-100f);
            translateAnimation.setToY(0f);
            translateAnimation.setInterpolator(EASE_IN);

            translateAnimation.setOnFinished(actionEvent -> {
                popup__account.setMouseTransparent(false);
                account__animating = false;
            });
            translateAnimation.play();
            fadeIn.play();

        } else if (!account__animating && popup__account.isVisible()) {
            account__animating = true;
            TranslateTransition translateAnimation = new TranslateTransition(Duration.millis(150),popup__account);
            FadeTransition fadeInAnimation = new FadeTransition(Duration.millis(150),popup__account);
            fadeInAnimation.setFromValue(1);
            fadeInAnimation.setToValue(0);
            fadeInAnimation.setInterpolator(EASE_IN);
            translateAnimation.setFromY(0);
            translateAnimation.setToY(-100f);
            translateAnimation.setInterpolator(EASE_IN);

            translateAnimation.setOnFinished(actionEvent -> {
                popup__account.setVisible(false);
                account__animating = false;
            });
            translateAnimation.play();
            fadeInAnimation.play();
            popup__account.setMouseTransparent(true);
        }
    }

    @FXML
    public void toggleNavbar__myBooksButton(){

        if(!myBooks__animating && !navbar__myBooks__arrow.getStyleClass().contains("upside")){
            navbar__myBooks__arrow.getStyleClass().add("upside");
            myBooks__animating = true;
            RotateTransition rotateUpwards = new RotateTransition(Duration.millis(300),navbar__myBooks__arrow);
            rotateUpwards.setFromAngle(0);
            rotateUpwards.setToAngle(180);

            rotateUpwards.setOnFinished(actionEvent -> {
                myBooks__animating = false;
            });

            rotateUpwards.play();
            dropAnimation();
        } else if (!myBooks__animating && navbar__myBooks__arrow.getStyleClass().contains("upside")) {
            navbar__myBooks__arrow.getStyleClass().remove("upside");
            myBooks__animating = true;
            RotateTransition rotateDownwards = new RotateTransition(Duration.millis(200),navbar__myBooks__arrow);
            rotateDownwards.setFromAngle(180);
            rotateDownwards.setToAngle(0);

            rotateDownwards.play();
            returnAnimation();
            rotateDownwards.setOnFinished(actionEvent -> {
                myBooks__animating = false;
            });

        }
    }



    @FXML
    private void dropAnimation(){
        popup__myBooks.setVisible(true);
        TranslateTransition translateAnimation = new TranslateTransition(Duration.millis(300),popup__myBooks);
        FadeTransition fadeInAnimation = new FadeTransition(Duration.millis(300),popup__myBooks);
        fadeInAnimation.setFromValue(0);
        fadeInAnimation.setToValue(1);
        fadeInAnimation.setInterpolator(EASE_IN);
        translateAnimation.setFromY(-100f);
        translateAnimation.setToY(0f);
        translateAnimation.setInterpolator(EASE_IN);


        translateAnimation.setOnFinished(actionEvent -> {
            popup__myBooks.setMouseTransparent(false);

        });

        translateAnimation.play();
        fadeInAnimation.play();
        popup__myBooks.setMouseTransparent(true);

    }


    @FXML
    private void returnAnimation(){
        TranslateTransition translateAnimation = new TranslateTransition(Duration.millis(200),popup__myBooks);
        FadeTransition fadeInAnimation = new FadeTransition(Duration.millis(200),popup__myBooks);
        fadeInAnimation.setFromValue(1);
        fadeInAnimation.setToValue(0);
        fadeInAnimation.setInterpolator(EASE_IN);
        translateAnimation.setFromY(0);
        translateAnimation.setToY(-100f);
        translateAnimation.setInterpolator(EASE_IN);


        translateAnimation.setOnFinished(actionEvent -> {
            popup__myBooks.setVisible(false);
        });

        translateAnimation.play();
        fadeInAnimation.play();
        popup__myBooks.setMouseTransparent(true);
    }


    private void slideInCheckBook(){
        TranslateTransition slideInAnimation = new TranslateTransition(Duration.millis(200),popup__checkBook);
        slideInAnimation.setFromX(700);
        slideInAnimation.setToX(0);
        slideInAnimation.setInterpolator(EASE_IN);
        slideInAnimation.setOnFinished(actionEvent -> {
            popup__checkBook.setMouseTransparent(false);
        });
        slideInAnimation.play();
        popup__checkBook.setVisible(true);
    }

    @FXML
    public void renderCheckBook(int id){
        popup__checkBook.setMouseTransparent(true);
        BookData bookDetails = getBookData(id);
        checkBook__bookImage.setImage(bookDetails.image);
        slideInCheckBook();
    }
    @FXML
    public void navbar__renderSearchButton(){

    }

    @FXML
    private void closeCheckBook(){
        popup__checkBook.setVisible(false);
        popup__checkBook.setMouseTransparent(true);
    }


    @FXML
    private void switchToMainPage(){
        replaceCenterPageContent(app.class.getResource("fxml/centerPages/homePage.fxml"), homePageController.class);
    }
    @FXML
    private void switchToSearchPage(){
        replaceCenterPageContent(app.class.getResource("fxml/centerPages/searchPage.fxml"), searchPageController.class);
    }

    @FXML
    private void switchToSearchPageBySearchBar(){
        renderCenterPageContentFromSearch(app.class.getResource("fxml/centerPages/searchPage.fxml"), searchPageController.class,navbar__searchBar.getText());
    }


    public void replaceCenterPageContent(URL fxmlResource, Class<?> controllerClass) {
    try {
        FXMLLoader loader = new FXMLLoader(fxmlResource);
        AnchorPane newContent = loader.load();
        Object controllerInstance = loader.getController();
        Method setMainPageControllerMethod = controllerClass.getMethod("setMainPageController", mainPageController.class);
        setMainPageControllerMethod.invoke(controllerInstance, this);
        navbar__searchBar.setText("");
        // Set the new content as the CenterPage content
        scrollContainer.setVvalue(0);
        scrollContainer.setContent(newContent);
    } catch (IOException e) {
        e.printStackTrace();
        // Handle exception if loading FXML fails
    } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
        throw new RuntimeException(e);
    }
    }

    @FXML
    public void renderCenterPageContentFromSearch(URL fxmlResource, Class<?> controllerClass,String searchText){
        try {
            FXMLLoader loader = new FXMLLoader(fxmlResource);
            AnchorPane newContent = loader.load();
            Object controllerInstance = loader.getController();
            Method setSearchBarText = controllerClass.getMethod("setSearchBarText", String.class);
            Method setMainPageControllerMethod = controllerClass.getMethod("setMainPageController", mainPageController.class);
            setSearchBarText.invoke(controllerInstance,searchText);
            setMainPageControllerMethod.invoke(controllerInstance, this);

            // Set the new content as the CenterPage content
            scrollContainer.setVvalue(0);
            scrollContainer.setContent(newContent);
        } catch (IOException e) {
            e.printStackTrace();
            // Handle exception if loading FXML fails
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    private void renderBooksOnEnterFromSearchBar(KeyEvent ev){
        if(ev.getCode() == KeyCode.ENTER) switchToSearchPageBySearchBar();
    }


}