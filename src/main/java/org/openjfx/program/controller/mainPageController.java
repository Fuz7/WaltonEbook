package org.openjfx.program.controller;

import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.InnerShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.transform.Rotate;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.openjfx.program.app;
import org.openjfx.program.database.ReturnData;
import org.openjfx.program.model.BookData;
import org.openjfx.program.pdfCreator;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

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
    public ScrollPane scrollContainer;
    @FXML
    private ImageView checkBook__bookImage;
    @FXML
    private StackPane popup__checkBook;
    @FXML
    private StackPane checkBook__closeButton;
    @FXML
    private TextField navbar__searchBar;
    @FXML
    public ImageView profileLogo;
    @FXML
    private ScrollPane popup__scrollCheckBook;
    @FXML
    private Text checkBook__bookTitle;
    @FXML
    private Label checkBook__bookAuthor;
    @FXML
    private Label checkBook__bookRating;
    @FXML
    private Label checkBook__soldCount;
    @FXML
    private Text checkBook__descriptionBody;
    @FXML
    private Text checkBook__genreBody;
    @FXML
    private Button checkBook__purchaseButton;
    @FXML
    private Label checkBook__priceTag;
    @FXML
    private HBox checkBook__ratingStarContainer;
    @FXML
    private Button checkBook__reviewBookButton;
    @FXML
    private VBox checkBook__reviewTextAreaContainer;
    @FXML
    private HBox checkBook__currentReviewContainer;
    @FXML
    private TextArea checkBook__reviewTextArea;
    @FXML
    private ImageView checkBook__currentReviewImage;
    @FXML
    private Text checkBook__currentReviewBody;
    @FXML
    private Text checkBook__currentReviewUsername;
    @FXML
    private Button checkBook_reviewSubmitButton;
    @FXML
    private HBox checkBook__shopperRatingImageContainer;
    @FXML
    private HBox checkBook__anonymousRatingImageContainer;
    @FXML
    private Label checkBook__shopperRatingLabel;
    @FXML
    private Label checkBook__anonymousRatingLabel;
    @FXML
    private VBox checkBook__reviewContainer;
    @FXML
    private ImageView checkBookContainer__downloadIcon;
    @FXML
    private StackPane checkBook__downloadContainer;
    @FXML
    private Button checkBookContainer__downloadButton;
    @FXML
    private StackPane popup__receiptContainer;
    @FXML
    private Text receipt__balance;
    @FXML
    private Text receipt__cost;
    @FXML
    private Text receipt__newBalance;
    @FXML
    private Text receipt__bookName;
    @FXML
    private ImageView receipt__image;
    @FXML
    private Text receipt__header;
    @FXML
    private Label checkBook__year;
    @FXML
    private Text checkBook__language;
    @FXML
    private Text checkBook__isbn;



    private boolean myBooks__animating = false;
    private boolean account__animating = false;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        hidePopups();
        try {
            setNavbarRoundedImage(profileLogo,returnAccountImage());
            checkBookContainer__downloadIcon.setMouseTransparent(true);

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    public void hidePopups(){
        popup__myBooks.setMouseTransparent(true);
        popup__myBooks.setVisible(false);
        popup__account.setMouseTransparent(true);
        popup__account.setVisible(false);
        popup__checkBook.setMouseTransparent(true);
        popup__checkBook.setVisible(false);
        popup__receiptContainer.setMouseTransparent(true);
        popup__receiptContainer.setVisible(false);
    }

    public BookData getBookData(int book_id){
        String[] value = app.db.Return.returnBookDataById(book_id);
        String title = (String) value[1];
        String description = (String) value[2];
        String formattedDescription = description.replaceAll("\\n", " ");
        String imageLink = (String) value[3];
        Path sourceDirectory = Paths.get("src/main/resources/org/openjfx/program/images/books/");

        Path correctPath = Paths.get(sourceDirectory.resolve(imageLink).toUri());
        File imageFile = new File(correctPath.toString());
        String absoluteImageUrl = null;
        try {
            absoluteImageUrl = imageFile.toURI().toURL().toString();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        Image image = new Image(absoluteImageUrl);
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
        popup__scrollCheckBook.setVvalue(0);
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
        checkBook__bookTitle.setText(bookDetails.title);
        checkBook__bookAuthor.setText(app.db.Return.returnAuthorNameByID(id));
        renderDownloadButton(id,app.lm.getSessionId());
        checkBookContainer__downloadButton.setOnMouseClicked(mouseEvent -> {
            try {
                downloadFile(id);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        renderOverallRatings(id);
        checkBook__soldCount.setText("Sold: " + bookDetails.bookSold);
        checkBook__descriptionBody.setText(bookDetails.formattedDescription);
        String stringGenre = arrayListToStringWithSpace(app.db.Return.returnAllGenreById(id));
        checkBook__genreBody.setText(stringGenre);
        renderPurchaseButton(id);
        checkBook__purchaseButton.setOnMouseClicked(mouseEvent ->  purchaseButtonOnClick(id));

        checkBook__priceTag.setText(String.format("%.2f$",bookDetails.price));
        receipt__cost.setText(String.format("- %.2f $",bookDetails.price));

        String username = app.db.Return.returnUserName(app.lm.getSessionId());
        receipt__header.setText("Thank you "+  username + " for your recent transaction on WaltonEBook");
        receipt__image.setImage(bookDetails.image);
        double balance = app.db.Return.returnUserCash(app.lm.getSessionId());
        receipt__balance.setText(String.format(" %.2f $",balance));
        double newBalance = balance - bookDetails.price;
        receipt__newBalance.setText(String.format(" %.2f $",newBalance));
        receipt__bookName.setText(bookDetails.title);

        renderRatingStarsEvents(id,app.lm.getSessionId());
        renderRatingStarsClass(id,app.lm.getSessionId());
        checkBook_reviewSubmitButton.setOnMouseClicked(mouseEvent ->  renderReviewSubmitTextArea(id,app.lm.getSessionId()));
        slideInCheckBook();
        renderReviewText(id,app.lm.getSessionId());
        renderAverageRatings(id);
        renderReviews(id);
    }

    private void renderDownloadButton(int bookId, int userId){
        checkBook__downloadContainer.setVisible(app.db.Check.CheckIfBookWasBought(bookId, userId));
    }

    private void renderPurchaseButton(int bookId){
        checkBook__purchaseButton.getStyleClass().remove("checkBook__purchaseButton--insufficient");
        checkBook__purchaseButton.getStyleClass().remove("checkBook__purchaseButton--bought");
        checkBook__purchaseButton.setText("Purchase");
        if(app.db.Check.CheckIfBookWasBought(bookId,app.lm.getSessionId())){
            checkBook__purchaseButton.getStyleClass().add("checkBook__purchaseButton--bought");
            checkBook__purchaseButton.setText("Bought");
        }else{
            checkBook__purchaseButton.getStyleClass().remove("checkBook__purchaseButton--bought");
            checkBook__purchaseButton.setText("Purchase");

        }
    }

    private void purchaseButtonOnClick(int bookId){
        if(!checkBook__purchaseButton.getStyleClass().contains("checkBook__purchaseButton--bought")){
            if(!app.db.Update.buyBook(app.lm.getSessionId(), bookId)){
                checkBook__purchaseButton.getStyleClass().add("checkBook__purchaseButton--insufficient");
            }else{
                popup__checkBook.setVisible(false);
                checkBook__purchaseButton.getStyleClass().add("checkBook__purchaseButton--bought");
                popup__receiptContainer.setVisible(true);
                popup__receiptContainer.setMouseTransparent(false);

            }
        }
    }
    private ImageView getChildAtIndex(StackPane parent, int index) {
        // Retrieve the child at the specified index
        if (index >= 0 && index < parent.getChildren().size()) {
            return (ImageView) parent.getChildren().get(index);
        } else {
            return null; // Index out of bounds
        }
    }

    private void renderRatingStarsEvents(int bookId, int userId){
        checkBook__ratingStarContainer.getChildren().forEach(node ->{
            if(node instanceof ImageView imageView){
               imageView.setOnMouseClicked(mouseEvent -> handleRatingStarClick(mouseEvent,bookId,userId));
            }
        });
    }

    private void renderRatingStarsClass(int bookId, int userId){
        checkBook__ratingStarContainer.getChildren().forEach(node ->{
            if(node instanceof ImageView imageView){
                imageView.getStyleClass().remove("checkBook__ratingStar--rated");
                imageView.getStyleClass().remove("checkBook__ratingStar--bought");
                if(app.db.Check.CheckIfBookWasBought(bookId,userId)){
                    imageView.getStyleClass().add("checkBook__ratingStar--bought");
                }
            }

        });

        if(app.db.Check.checkIfRatingExist(bookId,userId)){
            int ratingScore = app.db.Return.returnReviewRating(userId,bookId);
            for(int i = 0;i < ratingScore;i++){
                ImageView currStar = (ImageView) checkBook__ratingStarContainer.getChildren().get(i);
                currStar.getStyleClass().add("checkBook__ratingStar--rated");
            }
        }

    }

    private void handleRatingStarClick(MouseEvent ev,int bookId, int userId) {
        // Retrieve the index of the clicked child
        ImageView star = (ImageView) ev.getSource();
        HBox parent = (HBox) star.getParent();
        int index = parent.getChildren().indexOf(star);
        int ratingScore = index + 1;
        for(int i = 0;i < 5;i++){
            ImageView currStar = (ImageView) parent.getChildren().get(i);
            currStar.getStyleClass().remove("checkBook__ratingStar--rated");
        }
        for(int i = 0;i < ratingScore;i++){
            ImageView currStar = (ImageView) parent.getChildren().get(i);
            currStar.getStyleClass().add("checkBook__ratingStar--rated");
        }
        app.db.Insert.InsertBookRating(bookId,userId,ratingScore);
        renderAverageRatings(bookId);
        renderReviews(bookId);
        renderOverallRatings(bookId);
    }

    private void renderReviewText(int bookId,int userId) {
        if(app.db.Check.checkIfReviewTextExist(bookId,userId)){
            checkBook__reviewTextAreaContainer.setVisible(false);
            checkBook__currentReviewContainer.setVisible(true);
            checkBook__reviewBookButton.setText("Reviewed");
            try {
                setReviewRoundedCorner(checkBook__currentReviewImage,returnUserImage(userId));

            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            checkBook__currentReviewUsername.setText(app.db.Return.returnAuthorNameByID(app.lm.getSessionId()));
            checkBook__currentReviewBody.setText(app.db.Return.returnReviewText(userId,bookId));
            checkBook__reviewBookButton.getStyleClass().add("checkBook__reviewBookButton--reviewed");

        }else{
            checkBook__reviewBookButton.setText("Review Book");
            checkBook__reviewTextAreaContainer.setVisible(false);
            checkBook__currentReviewContainer.setVisible(false);
            checkBook__reviewTextArea.setText("");
            checkBook__reviewBookButton.getStyleClass().remove("checkBook__reviewBookButton--reviewed");
        }
    }

    @FXML
    private void reviewBookOnClick(){
        if(!checkBook__reviewBookButton.getStyleClass().contains("checkBook__reviewBookButton--reviewed")){
            checkBook__reviewTextAreaContainer.setVisible(true);
        }

    }

    @FXML
    private void reviewCancelTextAreaOnClick(){
        checkBook__reviewTextAreaContainer.setVisible(false);
        checkBook__reviewTextArea.setText("");
    }

    private void renderReviewSubmitTextArea(int bookId, int userId){
        checkBook__reviewTextAreaContainer.setVisible(false);
        app.db.Insert.InsertBookReviewText(bookId,userId,checkBook__reviewTextArea.getText());
        checkBook__currentReviewContainer.setVisible(true);
        checkBook__reviewTextArea.setText("");
        try {
            setReviewRoundedCorner(checkBook__currentReviewImage,returnAccountImage());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        checkBook__currentReviewUsername.setText(app.db.Return.returnAuthorNameByID(app.lm.getSessionId()));
        checkBook__currentReviewBody.setText(app.db.Return.returnReviewText(userId,bookId));
        checkBook__reviewBookButton.getStyleClass().add("checkBook__reviewBookButton--reviewed");
        checkBook__reviewBookButton.setText("Reviewed");
        renderReviews(bookId);

    }

    private void  renderOverallRatings(int bookId){
        int ratingSum  = 0;
        int ratingCount = 0;
        List<int[]> bookRatings = app.db.Return.returnBookReviewRatings(bookId);
        for (int[] idAndRating : bookRatings) {
            ratingSum+=  idAndRating[1];
            ratingCount +=1;
        }
        float averageRating = (float) ratingSum / ratingCount;
        if(Float.isNaN(averageRating)){
            checkBook__bookRating.setText("Ratings: 0.00");
        }else{
            checkBook__bookRating.setText("Ratings: " +  String.format("%.2f",averageRating));
        }
    }

    private void renderAverageRatings(int bookId){
        checkBook__shopperRatingImageContainer.getChildren().clear();
        checkBook__anonymousRatingImageContainer.getChildren().clear();
        List<int[]> bookRatings = app.db.Return.returnBookReviewRatings(bookId);
        int shopperSum = 0;
        int anonymousSum = 0;
        int shopperCount = 0;
        int anonymousCount = 0;
        for (int[] idAndRating : bookRatings) {
            if (app.db.Check.CheckIfBookWasBought(bookId, idAndRating[0])) {
                shopperSum += idAndRating[1];
                shopperCount += 1;
            } else if (!app.db.Check.CheckIfBookWasBought(bookId, idAndRating[0])) {
                anonymousSum += idAndRating[1];
                anonymousCount += 1;
            }
        }
        float averageShopperRating = (float) shopperSum / shopperCount;
        float averageAnonymousRating = (float) anonymousSum / anonymousCount;
        if(Float.isNaN(averageShopperRating)){
            checkBook__shopperRatingLabel.setText("0.00");
        }else{
            checkBook__shopperRatingLabel.setText(String.format("%.2f",averageShopperRating));
        }
        if (Float.isNaN(averageAnonymousRating)) {
            checkBook__anonymousRatingLabel.setText("0.00");
        }else{
            checkBook__anonymousRatingLabel.setText(String.format("%.2f",averageAnonymousRating));
        }

        for(float i = averageShopperRating;i > 0;i--){
            if(i >= 1){
                addShopperStar();
            }else{
                addShopperStar(i);
            }
        }

        for (float i = averageAnonymousRating; i  > 0;i--){
            if(i >= 1){
                addAnonymousStar();
            }else{
                addAnonymousStar(i);
            }
        }

    }

    private void renderReviews(int bookId){
        checkBook__reviewContainer.getChildren().clear();
        List<String[]> bookReviews = app.db.Return.returnAllBookReviewsById(bookId);
        for(int i = 0;i <bookReviews.size();i++){
            String[] bookReview = bookReviews.get(i);
            String bookReviewData  = bookReview[1];
            if(!bookReviewData.isEmpty()){
                int userId = Integer.parseInt(bookReview[0]);
                int rating = app.db.Return.returnReviewRating(userId,bookId);
                boolean isOwned =  app.db.Check.CheckIfBookWasBought(bookId, userId);
                renderReviewCard(userId,rating,isOwned,bookReviewData);
            }
        }
    }

    private void renderReviewCard(int userId,int rating, boolean isOwned,String review){
        HBox cardContainer =  new HBox();
        cardContainer.minWidth(460);
        cardContainer.setAlignment(Pos.TOP_LEFT);

        VBox imageAndNameContainer = new VBox();
        imageAndNameContainer.setAlignment(Pos.TOP_LEFT);
        imageAndNameContainer.prefWidth(137);
        imageAndNameContainer.maxWidth(137);
        ImageView profileImage = createProfileContainer();
        try {
            setReviewRoundedCorner(profileImage,returnUserImage(userId));
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        Text nameText = new Text();
        nameText.setWrappingWidth(137);
        nameText.setTextAlignment(TextAlignment.LEFT);
        nameText.setText(app.db.Return.returnUserUserName(userId));
        nameText.getStyleClass().add("checkBook__reviewerName");

        imageAndNameContainer.getChildren().addAll(profileImage,nameText);

        cardContainer.getChildren().add(imageAndNameContainer);


        VBox starAndTextContainer = new VBox();
        starAndTextContainer.setAlignment(Pos.TOP_LEFT);
        starAndTextContainer.setPadding(new javafx.geometry.Insets(0, 0, 0, 4));
        starAndTextContainer.prefWidth(317);
        starAndTextContainer.maxWidth(317);

        HBox starContainer = new HBox();
        starContainer.setAlignment(Pos.BOTTOM_LEFT);
        starContainer.setSpacing(10);
        starContainer.prefWidth(317);
        starContainer.prefHeight(45);
        starContainer.maxWidth(317);
        starContainer.maxHeight(45);
        for(int i = rating;i > 0;i--){
            ImageView imageContainer = createFortyPxStarContainer();
            if(isOwned){
                addShopperStar(imageContainer);
            }else{
                addAnonymousStar(imageContainer);
            }
            starContainer.getChildren().add(imageContainer);
        }
        starAndTextContainer.getChildren().add(starContainer);
        Separator separator = new Separator();
        separator.setMinHeight(3);
        separator.setPrefWidth(317);
        separator.setMinWidth(317);
        VBox.setMargin(separator,new Insets(10,0,0,0));
        separator.setEffect(new InnerShadow(21,Color.color(0,0,0,0.75)));
        starAndTextContainer.getChildren().add(separator);
        Text reviewText = new Text();
        reviewText.setTextAlignment(TextAlignment.LEFT);
        reviewText.setWrappingWidth(317);
        reviewText.getStyleClass().add("checkBook__reviewText");
        reviewText.setLineSpacing(2);
        VBox.setMargin(reviewText, new Insets(3,0,30,0));
        reviewText.setText(review);
        starAndTextContainer.getChildren().add(reviewText);
        cardContainer.getChildren().add(starAndTextContainer);
        checkBook__reviewContainer.getChildren().add(cardContainer);
    }

    private void addShopperStar(){
        ImageView imageContainer  = createStarContainer();
        Image purpleStar =  new Image(Objects.requireNonNull(app.class.getResource("images/purpleStar.png")).toString());
        imageContainer.setImage(purpleStar);
        checkBook__shopperRatingImageContainer.getChildren().add(imageContainer);
    }


    private void addShopperStar(ImageView imageContainer){
        Image purpleStar =  new Image(Objects.requireNonNull(app.class.getResource("images/purpleStar.png")).toString());
        imageContainer.setImage(purpleStar);

    }
    private void addAnonymousStar(ImageView imageContainer){
        Image goldStar =  new Image(Objects.requireNonNull(app.class.getResource("images/goldStar.png")).toString());
        imageContainer.setImage(goldStar);

    }


    private void addShopperStar(float ratingValue){
        ImageView  imageContainer = createStarContainer();
        Image purpleStar =  new Image(Objects.requireNonNull(app.class.getResource("images/purpleStar.png")).toString());
        Rectangle clip = new Rectangle((ratingValue*30),30);
        imageContainer.setClip(clip);
        imageContainer.setImage(purpleStar);
        checkBook__shopperRatingImageContainer.getChildren().add(imageContainer);
    }



    private void addAnonymousStar(){
        ImageView imageContainer  = createStarContainer();
        Image goldStar =  new Image(Objects.requireNonNull(app.class.getResource("images/goldStar.png")).toString());
        imageContainer.setImage(goldStar);
        checkBook__anonymousRatingImageContainer.getChildren().add(imageContainer);
    }

    private void addAnonymousStar(float ratingValue){
        ImageView  imageContainer = createStarContainer();
        Image goldStar =  new Image(Objects.requireNonNull(app.class.getResource("images/goldStar.png")).toString());
        Rectangle clip = new Rectangle((ratingValue*30),30);
        imageContainer.setClip(clip);
        imageContainer.setImage(goldStar);
        checkBook__anonymousRatingImageContainer.getChildren().add(imageContainer);
    }

    private ImageView createStarContainer(){
        ImageView imageContainer  = new ImageView();
        imageContainer.setFitWidth(30);
        imageContainer.setFitHeight(30);
        imageContainer.setPreserveRatio(true);
        imageContainer.setSmooth(true);
        return imageContainer;
    }

    private ImageView createFortyPxStarContainer(){
        ImageView imageContainer  = new ImageView();
        imageContainer.setFitWidth(40);
        imageContainer.setFitHeight(40);
        imageContainer.setPreserveRatio(true);
        imageContainer.setSmooth(true);
        return imageContainer;
    }
    private ImageView createProfileContainer(){
        ImageView imageContainer  = new ImageView();
        imageContainer.setFitWidth(51);
        imageContainer.setFitHeight(51);
        imageContainer.setPreserveRatio(false);
        imageContainer.setSmooth(true);
        return imageContainer;
    }


    private String arrayListToStringWithSpace(List<String> genre){
        StringJoiner joiner = new StringJoiner(", ");
        for (String element: genre){
            joiner.add(element);
        }
        String concatenatedString = joiner.toString();
        return concatenatedString;
    }


    @FXML
    private void closeCheckBook(){
        popup__checkBook.setVisible(false);
        popup__checkBook.setMouseTransparent(true);
    }

    @FXML
    private void closeReceipt(){
        popup__receiptContainer.setVisible(false);
        popup__receiptContainer.setMouseTransparent(true);
    }


    @FXML
    private void logout() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(app.class.getResource("fxml/loginPage.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        Stage currentStage = (Stage) navbar__myBooks__arrow.getScene().getWindow();
        currentStage.setTitle("Login Page");
        // Center Screen
        currentStage.setX(0);
        currentStage.setY(0);
        app.lm.setSessionId(0);
        currentStage.setScene(scene);
        currentStage.show();
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
    private void switchToAccountPage(){
        replaceCenterPageContent(app.class.getResource("fxml/centerPages/accountPage.fxml"),accountPageController.class);
    }

    @FXML
    private void switchToAboutUsPage(){
        replaceCenterPageContent(app.class.getResource("fxml/centerPages/aboutUsPage.fxml"), aboutUsController.class);

    }

    @FXML
    private void switchToBookOwnedPage(){
        replaceCenterPageContent(app.class.getResource("fxml/centerPages/bookOwnedPage.fxml"), bookOwnedController.class);

    }

    @FXML
    public void switchToBookPublishedPage(){
        replaceCenterPageContent(app.class.getResource("fxml/centerPages/bookPublished.fxml"), bookPublishedController.class);

    }

    @FXML
    private void switchToSearchPageBySearchBar(){
        renderCenterPageContentFromSearch(app.class.getResource("fxml/centerPages/searchPage.fxml"), searchPageController.class,navbar__searchBar.getText());
    }

    public void switchToAddBookPage(){
        replaceCenterPageContent(app.class.getResource("fxml/centerPages/addBookPage.fxml"), addBookPageController.class);

    }



    public void replaceCenterPageContent(URL fxmlResource, Class<?> controllerClass) {
    try {
        FXMLLoader loader = new FXMLLoader(fxmlResource);
        AnchorPane newContent = loader.load();
        Object controllerInstance = loader.getController();
        Method setMainPageControllerMethod = controllerClass.getMethod("setMainPageController", mainPageController.class);
        setMainPageControllerMethod.invoke(controllerInstance, this);
        navbar__searchBar.setText("");
        navbar__myBooks__arrow.getStyleClass().remove("upside");
        RotateTransition rotateDownwards = new RotateTransition(Duration.millis(10),navbar__myBooks__arrow);
        rotateDownwards.setToAngle(0);
        rotateDownwards.play();
        popup__myBooks.setVisible(false);
        popup__myBooks.setMouseTransparent(true);
        popup__checkBook.setVisible(false);
        popup__checkBook.setMouseTransparent(true);
        popup__account.setVisible(false);
        popup__account.setMouseTransparent(true);
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
            navbar__myBooks__arrow.getStyleClass().remove("upside");
            RotateTransition rotateDownwards = new RotateTransition(Duration.millis(10),navbar__myBooks__arrow);
            rotateDownwards.setToAngle(0);
            rotateDownwards.play();
            popup__myBooks.setVisible(false);
            popup__checkBook.setVisible(false);
            popup__checkBook.setMouseTransparent(true);
            popup__account.setVisible(false);
            popup__account.setMouseTransparent(true);
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

    public void setAccountRoundedImage(ImageView container, String imageLink){
        Image im = new Image(imageLink,false);
        container.setImage(im);

        Rectangle clip = new Rectangle();
        clip.setWidth(200.0);
        clip.setHeight(200.0);

        clip.setArcHeight(15);
        clip.setArcWidth(15);
        clip.setStroke(Color.BLACK);
        container.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = container.snapshot(parameters,null);
        container.setClip(null);
        container.setEffect(new DropShadow(10, Color.LIGHTGRAY));
        container.setImage(image);
    }
    public void setReviewRoundedCorner(ImageView container, String imageLink){
        Image im = new Image(imageLink,false);
        container.setImage(im);

        Rectangle clip = new Rectangle();
        clip.setWidth(51.0);
        clip.setHeight(51.0);

        clip.setArcHeight(50);
        clip.setArcWidth(50);
        clip.setStroke(Color.TRANSPARENT);
        container.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = container.snapshot(parameters,null);
        container.setClip(null);
        container.setEffect(new DropShadow(10, Color.LIGHTGRAY));
        container.setImage(image);
    }


    public void setNavbarRoundedImage(ImageView container, String imageLink){
        Image im = new Image(imageLink,false);
        container.setImage(im);

        Rectangle clip = new Rectangle();
        clip.setWidth(59.0);
        clip.setHeight(60.0);

        clip.setArcHeight(60);
        clip.setArcWidth(60);
        clip.setStroke(Color.TRANSPARENT);
        container.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = container.snapshot(parameters,null);
        container.setClip(null);
        DropShadow shadow = new DropShadow(7,Color.BLACK);

        container.setEffect(shadow);
        container.setImage(image);
    }

    public String returnAccountImage() throws MalformedURLException {
        Path imageUrl = Paths.get("src/main/resources/org/openjfx/program/images/profile/"+ app.lm.getSessionId() +".jpg");
        if(!isExistingFile(imageUrl)){
            imageUrl = Paths.get("src/main/resources/org/openjfx/program/images/profile/defaultIcon.jpg");
        }
        Path absolutePath = Paths.get(System.getProperty("user.dir"), imageUrl.toString());
        File imageFile = new File(absolutePath.toString());
        String absoluteImageUrl = imageFile.toURI().toURL().toString();


        return absoluteImageUrl;
    }

    public String returnUserImage(int id) throws MalformedURLException {
        Path imageUrl = Paths.get("src/main/resources/org/openjfx/program/images/profile/"+ id +".jpg");
        if(!isExistingFile(imageUrl)){
            imageUrl = Paths.get("src/main/resources/org/openjfx/program/images/profile/defaultIcon.jpg");
        }
        Path absolutePath = Paths.get(System.getProperty("user.dir"), imageUrl.toString());
        File imageFile = new File(absolutePath.toString());
        String absoluteImageUrl = imageFile.toURI().toURL().toString();


        return absoluteImageUrl;
    }

    private static boolean isExistingFile(Path path) {
        return Files.exists(path) && Files.isRegularFile(path);
    }


    private void downloadFile(int bookId) throws IOException {
        DirectoryChooser directoryChooser = new DirectoryChooser();
        BookData bookData = getBookData(bookId);
        // Set the initial directory
        directoryChooser.setInitialDirectory(new File("C:/")); // Change this to your desired initial directory
        Stage currentStage = (Stage) checkBook__shopperRatingLabel.getScene().getWindow();

        // Show the directory chooser
        File selectedDirectory = directoryChooser.showDialog(currentStage);

        if (selectedDirectory != null) {
            System.out.println("Selected Directory: " + selectedDirectory.getAbsolutePath());
            Path sourceDirectory = Paths.get("src/main/resources/org/openjfx/program/images/books/");
            String imageLink =  getImageLinkName(bookData.image.getUrl());
            Path correctPath = Paths.get(sourceDirectory.resolve(imageLink).toUri());
            File imageFile = new File(correctPath.toString());
            String absoluteImageUrl = null;
            String dirPath = selectedDirectory.getAbsolutePath().toString();
            try {
                absoluteImageUrl = imageFile.toURI().toURL().toString();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            String genre = bookData.genre;
            pdfCreator.createBookPdf(absoluteImageUrl,bookData.title,bookData.formattedDescription,genre,dirPath);
        } else {
            System.out.println("No Directory Selected");
        }
    }

    private static String getImageLinkName(String imageUrl) throws MalformedURLException {
        URL url = new URL(imageUrl);
        String path = url.getPath();
        return path.substring(path.lastIndexOf('/') + 1);
    }

}