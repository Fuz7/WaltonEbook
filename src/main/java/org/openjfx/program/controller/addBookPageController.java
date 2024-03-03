package org.openjfx.program.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import org.openjfx.program.app;
import org.openjfx.program.database.InsertData;

import java.io.*;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.util.ArrayList;

public class addBookPageController {

    @FXML
    private ImageView addBook__imageContainer;
    @FXML
    private Text addBook__imageContainerError;
    @FXML
    private Label addBook__priceTextFieldLabel;
    @FXML
    private ImageView addBook__dollarSign;
    @FXML
    private TextField addBook__priceTextField;
    @FXML
    private Text addBook__priceFieldError;
    @FXML
    private TextField addBook__titleTextField;
    @FXML
    private Text addBook__titleFieldError;
    @FXML
    private TextArea addBook__descriptionTextArea;
    @FXML
    private GridPane addBook__checkBoxContainer;
    @FXML
    private Text addBook__genreError;
    @FXML
    private Button addBook__submitButton;

    private Path latestImageURI;

    public mainPageController mainPageController;

    public void setMainPageController(mainPageController mainPageController) {
        this.mainPageController = mainPageController;
        hideLabelOnNotEmpty();
        forceTextFieldToBeNumeric();
        hideAllErrorLabel();
        hideGenreErrorOnKeyPress();
    }

    private void hideAllErrorLabel(){
        addBook__imageContainerError.setVisible(false);
        addBook__priceFieldError.setVisible(false);
        addBook__titleFieldError.setVisible(false);
        addBook__genreError.setVisible(false);
    }

    private void renderPriceField(){

    }

    private void hideLabelOnNotEmpty(){
        addBook__priceTextFieldLabel.visibleProperty().bind(addBook__priceTextField.textProperty().isEmpty());
    }

    @FXML
    private void focusOnPriceFieldOnClick(){
        addBook__priceTextField.requestFocus();
    }

    private void forceTextFieldToBeNumeric(){
        addBook__priceTextField.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (!newValue.matches("\\d*\\.?\\d*")) {
                    addBook__priceTextField .setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }
    @FXML
    private void hidePriceErrorOnKeyPress(){
        addBook__priceFieldError.setVisible(false);
    }

    @FXML
    private void hideTitleErrorOnKeyPress(){
        addBook__titleFieldError.setVisible(false);
    }

    private boolean checkPriceFieldIfValid(){

        if(addBook__priceTextField.getText().trim().isEmpty()){
            addBook__priceFieldError.setVisible(true);
            return false;
        }
        if(addBook__priceTextField.getText().equals(".")){
            addBook__priceFieldError.setVisible(true);
            return false;
        }
        double price = Double.parseDouble(addBook__priceTextField.getText());
        if(price <= 0){
            addBook__priceFieldError.setVisible(true);
            return false;
        }
        return  true;
    }

    @FXML
    private void chooseImage() throws MalformedURLException {
        FileChooser fileChooser = new FileChooser();

        // Set a filter to allow only image files
        FileChooser.ExtensionFilter imageFilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.jpeg");
        fileChooser.getExtensionFilters().add(imageFilter);

        Path to;
        Path from;

        // Show open file dialog
        File selectedFile = fileChooser.showOpenDialog(null);

        if (selectedFile != null) {
            latestImageURI = Paths.get(selectedFile.toURI());

            File imageFile = new File(selectedFile.toString());
            String absoluteImageUrl = imageFile.toURI().toURL().toString();
            addBook__imageContainer.getStyleClass().remove("addBook__imageContainer--empty");
            addBook__imageContainerError.setVisible(false);
            setAddBookRoundedImage(addBook__imageContainer,absoluteImageUrl);
        }
    }
    public void setAddBookRoundedImage(ImageView container, String imageLink){
        Image im = new Image(imageLink,false);
        container.setImage(im);

        Rectangle clip = new Rectangle();
        clip.setWidth(246.0);
        clip.setHeight(359.0);

        clip.setArcHeight(10);
        clip.setArcWidth(10);
        clip.setStroke(Color.BLACK);
        container.setClip(clip);

        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        WritableImage image = container.snapshot(parameters,null);
        container.setClip(null);
        DropShadow shadow = new DropShadow(10, Color.color(0,0,0,0.25));
        shadow.setOffsetY(2);
        container.setEffect(shadow);
        container.setImage(image);
    }

    private boolean checkIfTitleFieldIsValid(){
        if(addBook__titleTextField.getText().trim().isEmpty()){
            addBook__titleFieldError.setText("Error, missing input");
            addBook__titleFieldError.setVisible(true);
            return false;
        }
        if(app.db.Check.checkIfBookTitleExists(addBook__titleTextField.getText())){
            addBook__titleFieldError.setText("Error, title already exist");
            addBook__titleFieldError.setVisible(true);
            return false;
        }
            return true;

    }


    private void hideGenreErrorOnKeyPress(){
        for (javafx.scene.Node node : addBook__checkBoxContainer.getChildren()) {
            if (node instanceof CheckBox checkBox) {
                checkBox.setOnAction(event -> addBook__genreError.setVisible(false));
            }
        }
    }

    private String[] returnAllActiveGenre(){
        ArrayList<String> genres = new ArrayList<>();
        for (javafx.scene.Node node : addBook__checkBoxContainer.getChildren()) {
            if (node instanceof CheckBox checkBox) {
                if(checkBox.isSelected()){
                    genres.add(checkBox.getText());
                }
            }
        }
        String[] arrayGenres = genres.toArray(new String[0]);

        return  arrayGenres;
    }

    @FXML
    private void onClickSubmitButton() throws IOException {
        boolean valid = true;
        if(addBook__imageContainer.getStyleClass().contains("addBook__imageContainer--empty")){
            addBook__imageContainerError.setVisible(true);
            valid = false;
        }
        if(!checkPriceFieldIfValid()){
            valid = false;
        }

        if(!checkIfTitleFieldIsValid()){
            valid = false;
        }

        if(returnAllActiveGenre().length == 0){
            addBook__genreError.setVisible(true);
            valid = false;
        }

            if(!valid){
            return;
        } else{
                String titleWithoutInvalidImageSymbol = removeSpecialCharacters(addBook__titleTextField.getText());
                Path sourceDirectory = Paths.get("src/main/resources/org/openjfx/program/images/books/");
                Path to = sourceDirectory.resolve( titleWithoutInvalidImageSymbol+".jpeg");
                if(!to.toFile().exists()){
                    copyFileUsingStream(latestImageURI.toFile(),to.toFile());
                    String title = addBook__titleTextField.getText();
                    String imageLink = String.valueOf(to.getFileName());
                    double price = Double.parseDouble(addBook__priceTextField.getText());
                    double roundedPrice = roundToTwoDecimalPlaces(price);
                    String description = addBook__descriptionTextArea.getText();
                    String[] genre = returnAllActiveGenre();
                    int authorId = app.db.Return.returnAuthorId(app.lm.getSessionId());
                    boolean isAdmin = app.db.Check.checkIfAdmin(app.lm.getSessionId());
                    app.db.Insert.InsertNewBook(title,imageLink,genre,authorId,isAdmin,roundedPrice,0,description);

                }
        }


    }
    public static double roundToTwoDecimalPlaces(double number) {
        // Create a DecimalFormat object with the pattern for two decimal places
        DecimalFormat decimalFormat = new DecimalFormat("#.##");

        // Format the number using the DecimalFormat and parse it back to double
        return Double.parseDouble(decimalFormat.format(number));
    }
    public static String removeSpecialCharacters(String input) {
        // Define a regular expression to match non-alphanumeric characters
        String regex = "[^a-zA-Z0-9 ]";

        // Replace all occurrences of non-alphanumeric characters with an empty string
        return input.replaceAll(regex, "");
    }

    private void copyFileUsingStream(File source, File dest) throws IOException {
        InputStream is = null;
        OutputStream os = null;


        try {
            is = new FileInputStream(source);
            os = new FileOutputStream(dest);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
        } finally {
            assert is != null;
            is.close();
            assert os != null;
            os.close();
        }
    }


}
