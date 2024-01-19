package org.openjfx.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class mainPageController implements Initializable {

    @FXML
    private HBox radioButtonsContainer;
    private Circle button1;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    private void clearAllRadioButtonClass(){
        // Access all buttons inside the HBox
        for (javafx.scene.Node node : radioButtonsContainer.getChildren()) {
            if (node instanceof Circle button) {
                button.getStyleClass().remove("radioButton--active");
            }
        }
    }

//  The Radio Button Uses Circle
    public void onRadioButtonClick(MouseEvent event){
        Circle buttonClicked = (Circle) event.getSource();
        clearAllRadioButtonClass();
        buttonClicked.getStyleClass().add("radioButton--active");
    }


}