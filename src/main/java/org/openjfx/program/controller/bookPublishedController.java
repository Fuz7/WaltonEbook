package org.openjfx.program.controller;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
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


public class bookPublishedController {

    @FXML
    private FlowPane booksPublished__publishedBooksContainer;
    @FXML
    private Label booksPublished__publishedBooksTitle;
    @FXML
    private FlowPane booksPublished__pendingBooksContainer;
    @FXML
    private Label booksPublished__pendingBooksTitle;

    public mainPageController mainPageController;

    public void setMainPageController(mainPageController mainPageController) {
        this.mainPageController = mainPageController;
        renderBooksPending();
        renderBooksPublished();
    }

    @FXML
    private void switchToAddBookPage(){
        this.mainPageController.switchToAddBookPage();
    }

    private void renderBooksPending(){
       ObservableList<Node> children = booksPublished__pendingBooksContainer.getChildren();

        for (int i = children.size() - 1; i > 0; i--) {
            children.remove(i);
        }
        List<Integer> bookIds = app.db.Return.returnBookPending(app.lm.getSessionId());
        booksPublished__pendingBooksTitle.setText("Books Pending : " + bookIds.size());
        for(int bookId: bookIds){
            renderBookCard(booksPublished__pendingBooksContainer,bookId,false);
        }

    }

    private void renderBooksPublished(){
        booksPublished__publishedBooksContainer.getChildren().clear();
        List<Integer> bookIds = app.db.Return.returnBookPublished(app.lm.getSessionId());
        booksPublished__publishedBooksTitle.setText("Books Published : " + bookIds.size());
        for(int bookId: bookIds){
            renderBookCard(booksPublished__publishedBooksContainer,bookId,true);
        }
    }


    private void renderBookCard(FlowPane container, int bookId,boolean haveEvent){
        BookData bookData = this.mainPageController.getBookData(bookId);
        VBox bookCard = new VBox();
        if(haveEvent){
            bookCard.setOnMouseClicked(mouseEvent -> this.mainPageController.renderCheckBook(bookId));
        }
        bookCard.setAlignment(Pos.TOP_CENTER);
        bookCard.getStyleClass().add("booksPublished__bookCard");
        bookCard.prefWidth(235);
        bookCard.prefHeight(350);
        bookCard.maxWidth(235);
        bookCard.maxHeight(350);
        bookCard.setEffect(new DropShadow(7, Color.color(0,0,0,0.55)));
        ImageView imageContainer = new ImageView();
        imageContainer.setPreserveRatio(false);
        imageContainer.setSmooth(true);
        imageContainer.setFitWidth(179);
        imageContainer.setFitHeight(276);
        VBox.setMargin(imageContainer, new Insets(18,0,0,0));
        setLatestEffectImage(imageContainer,bookData.image);
        StackPane textContainer = new StackPane();
        textContainer.setAlignment(Pos.CENTER);
        textContainer.getStyleClass().add("booksPublished__booksTextContainer");
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
        titleElement.getStyleClass().add("booksPublished__booksText");
        titleElement.setText(bookData.title);
        textContainer.getChildren().add(titleElement);
        bookCard.getChildren().addAll(imageContainer,textContainer);
        container.getChildren().add(bookCard);



    }

    public void setLatestEffectImage(ImageView container, Image ima){
        container.setImage(ima);

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
