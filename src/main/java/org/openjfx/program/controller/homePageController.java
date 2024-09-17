package org.openjfx.program.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import org.openjfx.program.app;
import org.openjfx.program.model.BookData;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
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
    @FXML
    private FlowPane homePage__LatestUploadContainer;
    @FXML
    private ImageView homePage__easterIcon;


    private static boolean fadeAnimating;
    public mainPageController mainPageController;

    public void setMainPageController(mainPageController mainPageController) {
        this.mainPageController = mainPageController;
        this.mainPageController.hidePopups();
        renderInitialization();

    }

    public void renderInitialization(){
        setUserDataForRadioButtons();
        radioButton1.getStyleClass().add("radioButton--active");
        radioButton1.getStyleClass().add("active");
        renderTopChoiceContent(1);
        String[] topThreeSoldBooks = app.db.Return.ReturnTopThreeBookTitle();
        int[] topThreeSoldId = new int[3];
        for(int i = 0;i < 3;i++){
            topThreeSoldId[i] = app.db.Return.returnBookIdByTitle(topThreeSoldBooks[i]);
        }

        HBox[] cards = {goldCard,silverCard,bronzeCard};
        for(int i = 0; i < cards.length;i++){
            int bookId =  topThreeSoldId[i];
            renderPopularContent(cards[i],bookId);
            renderPopularAnimation(cards[i]);
        }
        renderLatestUpload();
    }


    @Override
    public void initialize(URL locations, ResourceBundle resources) {



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


    private void renderTopChoiceContent(int id){

        BookData topChoiceBook = this.mainPageController.getBookData(id);
        String authorName = app.db.Return.returnAuthorNameByID(id);
        topChoice__title.setText(topChoiceBook.title);
        topChoice__description.setText(limitWithEllipsis(topChoiceBook.formattedDescription,360));
        topChoice__genre.setText(topChoiceBook.genre);
        topChoice__imageContainer.setImage(topChoiceBook.image);
        topChoice__imageContainer.setOnMouseClicked(mouseEvent -> this.mainPageController.renderCheckBook(id));
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
        BookData cardBookData = this.mainPageController.getBookData(id);
        String authorName = app.db.Return.returnAuthorNameByID(id);
        String ellipsesDescription = limitWithEllipsis(cardBookData.formattedDescription,320);
        String formattedPrice = String.format("%.2f$",cardBookData.price);
        hbox.setOnMouseClicked(mouseEvent -> this.mainPageController.renderCheckBook(id));
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
                title.setText(limitWithEllipsis(cardBookData.title,24));
                description.setText(ellipsesDescription);
                genre.setText(cardBookData.genre);
                price.setText(formattedPrice);
                sold.setText("Sold: "+cardBookData.bookSold);
            }
        }
    }


    @FXML
    private void renderEasterEgg(){
        int rand = (int) (Math.floor(Math.random() * 3) + 1);
        if(rand == 2){
            Path sourceDirectory = Paths.get("src/main/resources/org/openjfx/program/images/");
            String imageLink = "smilingWalton.png";
            Path correctPath = Paths.get(sourceDirectory.resolve(imageLink).toUri());
            File imageFile = new File(correctPath.toString());
            String absoluteImageUrl = null;
            try {
                absoluteImageUrl = imageFile.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            homePage__easterIcon.setImage(new Image(absoluteImageUrl));
        }else{
            Path sourceDirectory = Paths.get("src/main/resources/org/openjfx/program/images/");
            String imageLink = "waltonSleeping.png";
            Path correctPath = Paths.get(sourceDirectory.resolve(imageLink).toUri());
            File imageFile = new File(correctPath.toString());
            String absoluteImageUrl = null;
            try {
                absoluteImageUrl = imageFile.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }

            homePage__easterIcon.setImage(new Image(absoluteImageUrl));
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

    public void renderLatestUpload(){
        int[] latestBooks = app.db.Return.returnTenLatestBooks();
        for(int i = 0;i < 10; i++){
            VBox bookCard = (VBox)homePage__LatestUploadContainer.getChildren().get(i);
            renderLatestBookCard(bookCard,latestBooks[i]);

        }

    }

    public void renderLatestBookCard(VBox container,int bookId){
        container.setOnMouseClicked(mouseEvent -> this.mainPageController.renderCheckBook(bookId));
        BookData bookDetails = this.mainPageController.getBookData(bookId);
        ImageView imageContainer = (ImageView)container.getChildren().getFirst();
        setLatestEffectImage(imageContainer,bookDetails.image);
        StackPane textContainer = (StackPane) container.getChildren().get(1);
        Label titleElement = (Label)  textContainer.getChildren().getFirst();
        titleElement.setText(bookDetails.title);

    }
    public void setLatestEffectImage(ImageView container, Image im){
        container.setImage(im);

        Rectangle clip = new Rectangle();
        clip.setWidth(170.0);
        clip.setHeight(261.0);

        clip.setArcHeight(5);
        clip.setArcWidth(5);
        clip.setStroke(Color.TRANSPARENT);
        container.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = container.snapshot(parameters,null);
        container.setClip(null);
        InnerShadow shadow = new InnerShadow(7,Color.color(0,0,0,1));
        shadow.setOffsetY(2);
        container.setEffect(shadow);
        container.setImage(image);
    }



}