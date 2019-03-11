/**
 *
 *
 * @Author Thorkildsen Torje
 */

package controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class JoinGameController extends ViewComponent{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton JoinGameJoinButton;

    @FXML
    private JFXButton JoinGameCancelButton;

    @FXML
    private TableView<?> JoinGameGamesTable;

    @FXML
    void initialize() {
        assert JoinGameJoinButton != null : "fx:id=\"JoinGameJoinButton\" was not injected: check your FXML file 'JoinGameMenu.fxml'.";
        assert JoinGameCancelButton != null : "fx:id=\"JoinGameCancelButton\" was not injected: check your FXML file 'JoinGameMenu.fxml'.";
        assert JoinGameGamesTable != null : "fx:id=\"JoinGameGamesTable\" was not injected: check your FXML file 'JoinGameMenu.fxml'.";


        JoinGameCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });
    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) JoinGameCancelButton.getParent();
    }
}
