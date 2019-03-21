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

import com.mysql.cj.result.Row;
import database.Constants;
import database.DatabaseConnector;
import game.Game;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.BattleshipUser;

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
    private JFXButton joinGameReflexButton;

    @FXML
    private TableView<RowData> joinGameGamesTable;

    Game[] games;
    ObservableList<RowData> gameList = FXCollections.observableArrayList();

    TableColumn<RowData,String> hostColumn;
    TableColumn<RowData,Integer> winsColumn;
    TableColumn<RowData,Boolean> openColumn;

    @FXML
    void initialize() {
        assert joinGameJoinButton != null : "fx:id=\"joinGameJoinButton\" was not injected: check your FXML file 'JoinGameMenu.fxml'.";
        assert joinGameCancelButton != null : "fx:id=\"joinGameCancelButton\" was not injected: check your FXML file 'JoinGameMenu.fxml'.";
        assert joinGameGamesTable != null : "fx:id=\"joinGameGamesTable\" was not injected: check your FXML file 'JoinGameMenu.fxml'.";
        assert joinGameReflexButton != null : "fx:id=\"joinGameReflexButton\" was not injected: check your FXML file 'JoinGameMenu.fxml'.";

        hostColumn = new TableColumn<>("Host");
        hostColumn.setMinWidth(200);
        hostColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        winsColumn = new TableColumn<>("Wins");
        winsColumn.setMinWidth(100);
        winsColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));

        openColumn = new TableColumn<>("Open");
        openColumn.setMinWidth(150);
        openColumn.setCellValueFactory(new PropertyValueFactory<>("open"));

        refreshList();
        joinGameGamesTable.getColumns().addAll(hostColumn,winsColumn,openColumn);

        joinGameCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });

        joinGameJoinButton.setOnAction(event -> {
            startGame();
        });

        joinGameReflexButton.setOnAction(event -> {
            refreshList();
        });
    }
    public void refreshList(){
        DatabaseConnector newConnector = new DatabaseConnector(Constants.DB_URL);
        games = newConnector.getGames();
        gameList.clear();
        joinGameGamesTable.getItems().clear();


        for(int i=0; i<games.length; i++){
            boolean open = true;
            if(games[i].getJoinUser() != null) {
                open = false;
            }
            RowData newRow = new RowData(games[i].getHostUser().getUsername(),games[i].getHostUser().getWonGames(),open);
            gameList.add(newRow);
        }
        joinGameGamesTable.setItems(gameList);
    }



    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) joinGameCancelButton.getParent();
    }

    public class RowData{
        private String username;
        private int wins;
        private boolean open;

        public RowData(String username, int wins, boolean open){
            this.username = username;
            this.wins = wins;
            this.open = open;
        }
        public String getUsername(){
            return username;
        }
        public int getWins(){
            return wins;
        }
        public boolean getOpen(){
            return open;
        }
    }
}
