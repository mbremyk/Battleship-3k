/**
 *
 *
 * @Author Thorkildsen Torje
 */

package controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

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

        joinGameJoinButton.setOnAction(event -> {
            startGame();
        });
    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) joinGameCancelButton.getParent();
    }
}
