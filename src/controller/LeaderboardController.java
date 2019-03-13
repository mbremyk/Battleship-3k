package controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.TableView;
import javafx.scene.layout.AnchorPane;

public class LeaderboardController extends ViewComponent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<?> leaderboardTable;

    @FXML
    private JFXButton leaderboardMainMenuButton;

    @FXML
    void initialize() {
        assert leaderboardTable != null : "fx:id=\"leaderboardTable\" was not injected: check your FXML file 'LeaderboardMenu.fxml'.";
        assert leaderboardMainMenuButton != null : "fx:id=\"leaderboardCancelButton\" was not injected: check your FXML file 'LeaderboardMenu.fxml'.";

        leaderboardMainMenuButton.setOnAction(event -> {
            switchView("MainMenu");
        });


    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) leaderboardMainMenuButton.getParent();
    }

}
