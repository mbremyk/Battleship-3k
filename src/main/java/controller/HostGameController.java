/**
 * HostGameController.java
 *
 * <p>
 * Controller for the menu for naming and hosting a game
 * </p>
 *
 * @author Thorkildsen Torje
 * @author Grande Trym
 */

package controller;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.ResourceBundle;

import database.Constants;
import database.DatabaseConnector;
import game.Game;
import game.Statics;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import model.BattleshipUser;

public class HostGameController extends ViewComponent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton hostGameCreateGameButton;

    @FXML
    private TextField hostGameNameField;

    @FXML
    private PasswordField hostGamePasswordField;

    @FXML
    private JFXButton hostGameCancelButton;


    @FXML
    void initialize() {
        assert hostGameCreateGameButton != null : "fx:id=\"hostGameCreateGameButton\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert hostGameNameField != null : "fx:id=\"hostGameNameField\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert hostGamePasswordField != null : "fx:id=\"hostGamePasswordField\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert hostGameCancelButton != null : "fx:id=\"hostGameCancelButton\" was not injected: check your FXML file 'LoginMenu.fxml'.";

        hostGameCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });

        hostGameCreateGameButton.setOnAction(event -> {
            createGameButtonPressed();
        });

        hostGameNameField.setOnAction(event -> {
            createGameButtonPressed();
        });

        hostGamePasswordField.setOnAction(event -> {
            createGameButtonPressed();
        });
    }


    /**
     * Creates a new game in the database and brings the user to the game
     * The method returns false if the user isn't logged in
     *
     * @return true if user is logged in and the code is executed, false otherwise
     */
    public boolean createGameButtonPressed() {
        BattleshipUser user = Statics.getLocalUser();
        if (user != null) {
            DatabaseConnector databaseConnector4 = new DatabaseConnector(Constants.DB_URL);
            String gameName = hostGameNameField.getText();
            if (gameName.equals("")) gameName = user.getUsername() + "'s game";
            databaseConnector4.createGame(gameName);
            startGame();
            return true;
        }
        return false;
    }

    /**
     * Method to get the main AnchorPane of this controller's fxml file
     *
     * @return the main AnchorPane of this controller's fxml file
     */
    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) hostGameCancelButton.getParent();
    }
}
