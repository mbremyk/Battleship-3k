/**
 * MainMenuController.java
 *
 * <p>
 * Controller for the main menu
 * </p>
 *
 * @author Thorkildsen Torje
 */
package controller;

import com.jfoenix.controls.JFXButton;
import game.Statics;
import model.ConfirmBox;
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
            if(Statics.getLocalUser() != null) {
                switchView("HostGameMenu");
            }
            else{
                boolean login = ConfirmBox.display("You must be logged in to host a game! Log in?");
                if(login){
                    switchView("LoginMenu");
                }
            }
        });
        mainMenuLeaderboardButton.setOnAction(event -> {
            switchView("LeaderboardMenu");
        });
        mainMenuFeedbackButton.setOnAction(event -> {
            switchView("FeedbackMenu");
        });

    }

    /**
     * Method to get the main AnchorPane of this controller's fxml file
     *
     * @return the main AnchorPane of this controller's fxml file
     */
    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) mainMenuHostButton.getParent();
    }

}
