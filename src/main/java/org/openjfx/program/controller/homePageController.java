package org.openjfx.program.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.openjfx.program.app;
import org.openjfx.program.model.BookData;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;






public class homePageController implements Initializable {


    @FXML
    private HBox radioButtonsContainer;
    @FXML
    private Circle radioButton1;
    @FXML
    private ImageView topChoice__imageContainer;
    @FXML
    private Text topChoice__title;
    @FXML
    private Text topChoice__description;
    @FXML
    private Text topChoice__genre;
    @FXML
    private Text topChoice__author;
    @FXML
    private HBox goldCard;
    @FXML
    private HBox silverCard;
    @FXML
    private HBox bronzeCard;


    private static boolean fadeAnimating;
    public mainPageController mainPageController;

    public void setMainPageController(mainPageController mainPageController) {
        this.mainPageController = mainPageController;
        this.mainPageController.hidePopups();
    }
    @Override
    public void initialize(URL locations, ResourceBundle resources) {
        setUserDataForRadioButtons();
        radioButton1.getStyleClass().add("radioButton--active");
        radioButton1.getStyleClass().add("active");
        renderTopChoiceContent(1);
        String[] topThreeSoldBooks = app.db.ReturnTopThreeBooks();
        HBox[] cards = {goldCard,silverCard,bronzeCard};
        for(int i = 0; i < cards.length;i++){
            int bookId =  Integer.parseInt(topThreeSoldBooks[i]);
            renderPopularContent(cards[i],bookId);
            renderPopularAnimation(cards[i]);
        }


    }

    private void setUserDataForRadioButtons() {
        final int[] i = {1};
        radioButtonsContainer.getChildren().forEach(node -> {
            if (node instanceof Circle circle) {
                circle.setUserData(i[0]);
                i[0] +=1;
            }
        });
    }


    public static String limitWithEllipsis(String input, int maxLength) {
        if (input == null || input.length() <= maxLength) {
            return input;
        } else {
            return input.substring(0, maxLength - 3) + "...";
        }
    }

    private FadeTransition setTopChoiceAnimation(Node node,int book_id) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(200), node);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(200), node);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);

        fadeOut.setOnFinished(event -> {
            renderTopChoiceContent(book_id);
            fadeIn.play();
        });

        fadeIn.setOnFinished(event -> fadeAnimating = false);

        return fadeOut;
    }

    private BookData getBookData(int book_id){
        List<Object> value = app.db.ReturnBookDetailsById(book_id);
        String title = (String) value.get(1);
        String description = (String) value.get(2);
        String formattedDescription = description.replaceAll("\\n", " ");
        String imageLink = (String) value.get(3);
        String correctPath = String.valueOf(app.class.getResource("images/" + imageLink));

        Image image = new Image(correctPath);
        String genre = "Tags: " + value.get(4);
        int bookSold = (int) value.get(8);
        double price = (double)value.get(7);
        return new BookData(title, formattedDescription,image,genre, bookSold,price);
    }
    private void renderTopChoiceContent(int id){

        BookData topChoiceBook = getBookData(id);
        String authorName = app.db.ReturnAuthorNameById(id);
        topChoice__title.setText(topChoiceBook.title);
        topChoice__description.setText(topChoiceBook.formattedDescription);
        topChoice__genre.setText(topChoiceBook.genre);
        topChoice__imageContainer.setImage(topChoiceBook.image);
        topChoice__author.setText("Author: " + authorName);
    }

    private void renderPopularAnimation(HBox hbox){
        ScaleTransition scale = new ScaleTransition(Duration.millis(50), hbox);
        RotateTransition rotate = new RotateTransition(Duration.millis(50), hbox);

        // Set easing functions for smoother animation
        scale.setInterpolator(Interpolator.EASE_BOTH);
        rotate.setInterpolator(Interpolator.EASE_BOTH);

        hbox.setOnMouseEntered(event -> {
            rotate.setToAngle(-1.03);
            scale.setToX(1.01);
            scale.setToY(1.01);

            ParallelTransition parallelTransition = new ParallelTransition(scale, rotate);
            parallelTransition.play();
        });

        hbox.setOnMouseExited(event -> {
            rotate.setToAngle(0);
            scale.setToX(1);
            scale.setToY(1);

            ParallelTransition parallelTransition = new ParallelTransition(scale, rotate);
            parallelTransition.play();
        });


    }
    private void renderPopularContent(HBox hbox,int id){
        BookData cardBookData = getBookData(id);
        String authorName = app.db.ReturnAuthorNameById(id);
        String ellipsesDescription = limitWithEllipsis(cardBookData.formattedDescription,320);
        String formattedPrice = String. format("%.2f$",cardBookData.price);

        for (javafx.scene.Node node : hbox.getChildren()) {
            if (node instanceof ImageView imageContainer) {
                imageContainer.setImage(cardBookData.image);

            }
            if (node instanceof VBox textsParent){

                StackPane titleParent = (StackPane)textsParent.getChildren().getFirst();
                Text title = (Text) titleParent.getChildren().getFirst();
                HBox authorParent = (HBox) textsParent.getChildren().get(1);
                Text author = (Text)authorParent.getChildren().get(1);
                Text description = (Text) textsParent.getChildren().get(3);
                HBox genreParent = (HBox) textsParent.getChildren().get(4);
                Label genre = (Label) genreParent.getChildren().get(1);
                HBox priceParent = (HBox) textsParent.getChildren().get(5);
                Text price = (Text) priceParent.getChildren().get(1);
                StackPane soldParent = (StackPane) priceParent.getChildren().get(2);
                Text sold = (Text) soldParent.getChildren().getFirst();

                author.setText(authorName);
                title.setText(cardBookData.title);
                description.setText(ellipsesDescription);
                genre.setText(cardBookData.genre);
                price.setText(formattedPrice);
                sold.setText("Sold: "+cardBookData.bookSold);
            }
        }
    }

    private void clearAllRadioButtonClass(){        // Access all buttons inside the HBox
        for (javafx.scene.Node node : radioButtonsContainer.getChildren()) {
            if (node instanceof Circle button) {
                button.getStyleClass().remove("radioButton--active");
                button.getStyleClass().remove("active");

            }
        }
    }

    //  The Radio Button Uses Circle
    public void onRadioButtonClick(MouseEvent event){
        if(!fadeAnimating){
            Circle buttonClicked = (Circle) event.getSource();
            if(!buttonClicked.getStyleClass().contains("radioButton--active")){
                clearAllRadioButtonClass();
                buttonClicked.getStyleClass().add("radioButton--active");
                // To increase specificity level
                buttonClicked.getStyleClass().add("active");

                int book_id = ((int)buttonClicked.getUserData());

                FadeTransition titleAnimation = setTopChoiceAnimation(topChoice__title,book_id);
                FadeTransition genreAnimation = setTopChoiceAnimation(topChoice__genre,book_id);
                FadeTransition descriptionAnimation = setTopChoiceAnimation(topChoice__description,book_id);
                FadeTransition imageAnimation = setTopChoiceAnimation(topChoice__imageContainer,book_id);
                FadeTransition authorAnimation = setTopChoiceAnimation(topChoice__author,book_id);


                titleAnimation.play();
                genreAnimation.play();
                descriptionAnimation.play();
                authorAnimation.play();
                imageAnimation.play();
                fadeAnimating = true;
            }

        }

    }

    public void generateLatestUpload(){

    }


}