package org.openjfx.program.controller;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.stage.Stage;
import org.openjfx.program.scene_loader;


public class navbarController {
    public void Home_button(ActionEvent event)
    {

        scene_loader.loadScene("mainPage.fxml", "Log In", (Stage) ((Node) event.getSource()).getScene().getWindow());
    }

    public void Search_button(ActionEvent event)
    {
        System.out.println("WORK IN PROGRESS");
    }

    public void My_Book_button(ActionEvent event)
    {
        System.out.println("WORK IN PROGRESS");
    }

    public void Cash_In_button(ActionEvent event)
    {
        System.out.println("WORK IN PROGRESS");
    }

    public void About_Us_button(ActionEvent event)
    {
        System.out.println("WORK IN PROGRESS");
    }
}
