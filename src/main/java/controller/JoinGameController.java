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
    private JFXButton joinGameJoinButton;

    @FXML
    private JFXButton joinGameCancelButton;

    @FXML
    private TableView<?> joinGameGamesTable;

    @FXML
    void initialize() {
        assert joinGameJoinButton != null : "fx:id=\"joinGameJoinButton\" was not injected: check your FXML file 'JoinGameMenu.fxml'.";
        assert joinGameCancelButton != null : "fx:id=\"joinGameCancelButton\" was not injected: check your FXML file 'JoinGameMenu.fxml'.";
        assert joinGameGamesTable != null : "fx:id=\"joinGameGamesTable\" was not injected: check your FXML file 'JoinGameMenu.fxml'.";


        joinGameCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });
    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) joinGameCancelButton.getParent();
    }
}
