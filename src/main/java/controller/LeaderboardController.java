package controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;

import database.DatabaseConnector;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import model.BattleshipUser;
import database.Constants;

public class LeaderboardController extends ViewComponent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<BattleshipUser> leaderboardTable;

    @FXML
    private JFXButton leaderboardMainMenuButton;

    @FXML
    void initialize() {
        assert leaderboardTable != null : "fx:id=\"leaderboardTable\" was not injected: check your FXML file 'LeaderboardMenu.fxml'.";
        assert leaderboardMainMenuButton != null : "fx:id=\"leaderboardCancelButton\" was not injected: check your FXML file 'LeaderboardMenu.fxml'.";

        leaderboardMainMenuButton.setOnAction(event -> {
            switchView("MainMenu");
        });
        DatabaseConnector newConnector = new DatabaseConnector(Constants.DB_URL);
        BattleshipUser[] users = newConnector.getBattleshipUsers();
        ObservableList<BattleshipUser> userList = FXCollections.observableArrayList();

        TableColumn<BattleshipUser,String> nameColumn = new TableColumn<>("Username");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<BattleshipUser,Integer> winsColumn = new TableColumn<>("Wins");
        winsColumn.setMinWidth(100);
        winsColumn.setCellValueFactory(new PropertyValueFactory<>("wonGames"));

        TableColumn<BattleshipUser,Integer> lossColumn = new TableColumn<>("Losses");
        lossColumn.setMinWidth(100);
        lossColumn.setCellValueFactory(new PropertyValueFactory<>("lostGames"));


        for(int i=0; i<users.length; i++){
            userList.add(users[i]);
        }

        leaderboardTable.setItems(userList);
        leaderboardTable.getColumns().addAll(nameColumn,winsColumn,lossColumn);
    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) leaderboardMainMenuButton.getParent();
    }

}
