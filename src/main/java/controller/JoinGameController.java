/**
 * JoinGameController.java
 *
 * <p>
 * Controller for the menu for viewing and joining a game
 * </p>
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
import game.Statics;
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

public class JoinGameController extends ViewComponent {

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

    TableColumn<RowData, String> nameColumn;
    TableColumn<RowData, String> hostColumn;
    TableColumn<RowData, Integer> winsColumn;

    @FXML
    void initialize() {
        assert joinGameJoinButton != null : "fx:id=\"joinGameJoinButton\" was not injected: check your FXML file 'JoinGameMenu.fxml'.";
        assert joinGameCancelButton != null : "fx:id=\"joinGameCancelButton\" was not injected: check your FXML file 'JoinGameMenu.fxml'.";
        assert joinGameGamesTable != null : "fx:id=\"joinGameGamesTable\" was not injected: check your FXML file 'JoinGameMenu.fxml'.";
        assert joinGameReflexButton != null : "fx:id=\"joinGameReflexButton\" was not injected: check your FXML file 'JoinGameMenu.fxml'.";

        nameColumn = new TableColumn<>("Name");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("gameName"));

        hostColumn = new TableColumn<>("Host");
        hostColumn.setMinWidth(200);
        hostColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        winsColumn = new TableColumn<>("Wins");
        winsColumn.setMinWidth(100);
        winsColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));

        refreshList();
        joinGameGamesTable.getColumns().addAll(nameColumn, hostColumn, winsColumn);

        joinGameCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });

        joinGameJoinButton.setOnAction(event -> {
            joinButtonPressed();
        });

        joinGameReflexButton.setOnAction(event -> {
            refreshList();
        });
    }

    /**
     * Makes the user join the selected game
     *
     * @return true if a game has been selected and is joined, false otherwise
     */
    public boolean joinButtonPressed() {
        RowData rowData = joinGameGamesTable.getSelectionModel().getSelectedItem();
        if (Statics.getLocalUser() == null) {
            System.out.println("You are not logged in");
        } else if (rowData == null) {
            System.out.println("No game has been selected");
        } else {
            Game game = getGame(rowData.getUsername());
            DatabaseConnector databaseConnector = new DatabaseConnector();
            if (game.isGameOpen() && databaseConnector.joinGame(game)) {
                startGame();
                return true;
            } else {
                System.out.println("Could not join game");
            }
        }
        return false;

    }

    /**
     * Gets a game by the name of the host user
     *
     * @param hostUsername the name of the host user
     * @return the game with the specified host user, null if it doesn't exist
     */
    public Game getGame(String hostUsername) {
        for (Game game : games) {
            if (hostUsername.equals(game.getHostUser().getUsername())) return game;
        }
        return null;
    }

    /**
     * Refreshes the list of games, by reloading them from the database
     */
    public void refreshList() {
        DatabaseConnector newConnector = new DatabaseConnector(Constants.DB_URL);
        games = newConnector.getGames();
        gameList.clear();
        joinGameGamesTable.getItems().clear();


        for (int i = 0; i < games.length; i++) {
            boolean open = true;
            if (games[i].getJoinUser() != null) {
                open = false;
            }
            if (open) {
                RowData newRow = new RowData(games[i].getGameName(), games[i].getHostUser().getUsername(), games[i].getHostUser().getWonGames());
                gameList.add(newRow);
            }
        }
        joinGameGamesTable.setItems(gameList);
    }


    /**
     * Method to get the main AnchorPane of this controller's fxml file
     *
     * @return the main AnchorPane of this controller's fxml file
     */
    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) joinGameCancelButton.getParent();
    }

    public class RowData {
        private String gameName;
        private String username;
        private int wins;

        public RowData(String gameName, String username, int wins) {
            this.gameName = gameName;
            this.username = username;
            this.wins = wins;
        }

        public String getGameName() {
            return gameName;
        }

        public String getUsername() {
            return username;
        }

        public int getWins() {
            return wins;
        }
    }
}
