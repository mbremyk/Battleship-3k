package controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane rootPane;

    @FXML
    private JFXButton MainMenuLoginButton;

    @FXML
    private JFXButton MainMenuSignupButton;

    @FXML
    private JFXButton MainMenuHostButton;

    @FXML
    private JFXButton MainMenuJoinButton;

    @FXML
    private JFXButton MainMenuFeedbackButton;

    @FXML
    private JFXButton MainMenuOptionsButton;

    /**
     * Standard JavaFX method
     */

    @FXML
    void initialize() {
        assert MainMenuLoginButton != null : "fx:id=\"MainMenuLoginButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert MainMenuSignupButton != null : "fx:id=\"MainMenuSignupButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert MainMenuHostButton != null : "fx:id=\"MainMenuHostButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert MainMenuJoinButton != null : "fx:id=\"MainMenuJoinButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert MainMenuFeedbackButton != null : "fx:id=\"MainMenuFeedbackButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert MainMenuOptionsButton != null : "fx:id=\"MainMenuOptionsButton\" was not injected: check your FXML file 'MainMenu.fxml'.";

        MainMenuSignupButton.setOnAction(event -> {
            try {
                AnchorPane formPane = formPane = FXMLLoader.load(getClass().getResource("/view/SignupMenu.fxml"));
                rootPane.getChildren().setAll(formPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        MainMenuLoginButton.setOnAction(event -> {
            try {
                AnchorPane formPane = formPane = FXMLLoader.load(getClass().getResource("/view/LoginMenu.fxml"));
                rootPane.getChildren().setAll(formPane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }


}
