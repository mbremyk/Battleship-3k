/**
 * controller.LoginController.java
 *
 * @Author Thorkildsen Torje
 */
package controller;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController extends ViewComponent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton MainMenuHostButton;

    @FXML
    private JFXButton MainMenuJoinButton;

    @FXML
    private JFXButton MainMenuFeedbackButton;

    @FXML
    private JFXButton MainMenuLeaderboardButton;

    /**
     * Standard JavaFX method
     */

    @FXML
    void initialize() {
        assert MainMenuHostButton != null : "fx:id=\"MainMenuHostButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert MainMenuJoinButton != null : "fx:id=\"MainMenuJoinButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert MainMenuFeedbackButton != null : "fx:id=\"MainMenuFeedbackButton\" was not injected: check your FXML file 'MainMenu.fxml'.";
        assert MainMenuLeaderboardButton != null : "fx:id=\"MainMenuOptionsButton\" was not injected: check your FXML file 'MainMenu.fxml'.";

        MainMenuJoinButton.setOnAction(event -> {
            switchView("JoinGameMenu");
        });
        MainMenuHostButton.setOnAction(event -> {
            switchView("HostGameMenu");
        });
        MainMenuLeaderboardButton.setOnAction(event -> {
            switchView("LeaderboardMenu");
        });
        MainMenuFeedbackButton.setOnAction(event -> {
            switchView("FeedbackMenu");
        });

    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) MainMenuHostButton.getParent();
    }

}
