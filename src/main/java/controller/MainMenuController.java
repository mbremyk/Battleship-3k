/**
 * controller.MainMenuController.java
 *
 * @Author Thorkildsen Torje
 */
package main.java.controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController extends ViewComponent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton mainMenuHostButton;

    @FXML
    private JFXButton mainMenuJoinButton;

    @FXML
    private JFXButton mainMenuFeedbackButton;

    @FXML
    private JFXButton mainMenuLeaderboardButton;

    /**
     * Standard JavaFX method
     */

    @FXML
    void initialize() {
        assert mainMenuHostButton != null : "fx:id=\"mainMenuHostButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert mainMenuJoinButton != null : "fx:id=\"mainMenuJoinButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert mainMenuFeedbackButton != null : "fx:id=\"mainMenuFeedbackButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert mainMenuLeaderboardButton != null : "fx:id=\"mainMenuOptionsButton\" was not injected: check your FXML file 'MainMenu.fxml'.";


        mainMenuJoinButton.setOnAction(event -> {
            switchView("JoinGameMenu");
        });
        mainMenuHostButton.setOnAction(event -> {
            switchView("HostGameMenu");
        });
        mainMenuLeaderboardButton.setOnAction(event -> {
            switchView("LeaderboardMenu");
        });
        mainMenuFeedbackButton.setOnAction(event -> {
            switchView("FeedbackMenu");
        });

    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) mainMenuHostButton.getParent();
    }

}
