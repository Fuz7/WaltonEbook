package org.openjfx.program.controller;

import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.TextAlignment;
import org.openjfx.program.app;
import org.openjfx.program.model.BookData;

import java.util.List;

public class bookOwnedController {

    @FXML
    private FlowPane book_owned__myBooksContainer;
    @FXML
    private Label book_owned__myBooksTitle;

    public mainPageController mainPageController;

    public void setMainPageController(mainPageController mainPageController) {
        this.mainPageController = mainPageController;
        renderBookOwned();
    }

    private void renderBookOwned(){
        book_owned__myBooksContainer.getChildren().clear();
        List<Integer> bookIds = app.db.Return.returnOwnedBooks(app.lm.getSessionId());
        book_owned__myBooksTitle.setText("My Books : " + bookIds.size());
        for(int bookId: bookIds){
            renderBookCard(book_owned__myBooksContainer,bookId);
        }
    }

    private void renderBookCard(FlowPane container,int bookId){
        BookData bookData = this.mainPageController.getBookData(bookId);
        VBox bookCard = new VBox();
        bookCard.setOnMouseClicked(mouseEvent -> this.mainPageController.renderCheckBook(bookId));
        bookCard.setAlignment(Pos.TOP_CENTER);
        bookCard.getStyleClass().add("book_owned__myBookCard");
        bookCard.prefWidth(235);
        bookCard.prefHeight(350);
        bookCard.maxWidth(235);
        bookCard.maxHeight(350);
        bookCard.setEffect(new DropShadow(7,Color.color(0,0,0,0.55)));
        ImageView imageContainer = new ImageView();
        imageContainer.setPreserveRatio(false);
        imageContainer.setSmooth(true);
        imageContainer.setFitWidth(179);
        imageContainer.setFitHeight(276);
        VBox.setMargin(imageContainer, new Insets(18,0,0,0));
        setLatestEffectImage(imageContainer,bookData.image);
        StackPane textContainer = new StackPane();
        textContainer.setAlignment(Pos.CENTER);
        textContainer.getStyleClass().add("book_owned__myBooksTextContainer");
        VBox.setMargin(textContainer, new Insets(15,0,0,0));
        textContainer.setPrefHeight(41);
        textContainer.setMaxHeight(41);
        textContainer.setPrefWidth(235);
        Label titleElement = new Label();
        titleElement.setPadding(new Insets(0,15,0,15));
        titleElement.setTextAlignment(TextAlignment.CENTER);
        titleElement.prefWidth(210);
        titleElement.prefHeight(20);
        titleElement.maxWidth(210);
        titleElement.getStyleClass().add("book_owned__myBooksText");
        titleElement.setText(bookData.title);
        textContainer.getChildren().add(titleElement);
        bookCard.getChildren().addAll(imageContainer,textContainer);
        container.getChildren().add(bookCard);



    }

    public void setLatestEffectImage(ImageView container, Image im){
        container.setImage(im);

        Rectangle clip = new Rectangle();
        clip.setWidth(179.0);
        clip.setHeight(276.0);

        clip.setArcHeight(6);
        clip.setArcWidth(6);
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
