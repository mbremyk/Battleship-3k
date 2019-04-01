/**
 * HostGameController.java
 *
 * @Author Thorkildsen Torje
 * @Author Grande Trym
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
    }

    public void createGameButtonPressed() {
        DatabaseConnector databaseConnector = new DatabaseConnector(Constants.DB_URL);
        databaseConnector.createGame();
        startGame();
    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) hostGameCancelButton.getParent();
    }
}
