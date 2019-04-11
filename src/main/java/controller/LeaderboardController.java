package controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.Arrays;
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
    private TableView<LeaderboardRowData> leaderboardTable;

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
        BattleshipUser[] users = sortUsers(newConnector.getBattleshipUsers());
        ObservableList<LeaderboardRowData> rowData = FXCollections.observableArrayList();


        TableColumn<LeaderboardRowData,Integer> rankColumn = new TableColumn<>("Rank");
        rankColumn.setMinWidth(100);
        rankColumn.setCellValueFactory(new PropertyValueFactory<>("rank"));

        TableColumn<LeaderboardRowData,String> nameColumn = new TableColumn<>("Username");
        nameColumn.setMinWidth(200);
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<LeaderboardRowData,Integer> winsColumn = new TableColumn<>("Wins");
        winsColumn.setMinWidth(100);
        winsColumn.setCellValueFactory(new PropertyValueFactory<>("wins"));

        TableColumn<LeaderboardRowData,Integer> lossColumn = new TableColumn<>("Losses");
        lossColumn.setMinWidth(100);
        lossColumn.setCellValueFactory(new PropertyValueFactory<>("losses"));


        for(int i=0; i<users.length; i++){
            rowData.add(new LeaderboardRowData(i+1,users[i].getUsername(),users[i].getWonGames(),users[i].getLostGames()));
        }

        leaderboardTable.setItems(rowData);
        leaderboardTable.getColumns().addAll(rankColumn,nameColumn,winsColumn,lossColumn);
    }

    /**
     * Sorts the leaderboard by number of wins
     *
     * @param users the list of users to be sorted
     * @return a sorted list of users
     */
    private BattleshipUser[] sortUsers(BattleshipUser[] users){
      for(int i=0; i<users.length; i++){
          int firstPlace = i;
          for(int j=firstPlace+1; j<users.length; j++) {
              if (users[firstPlace].getWonGames() < users[j].getWonGames()) {
                  firstPlace = j;
              }
          }
          BattleshipUser copy = users[i];
          users[i] = users[firstPlace];
          users[firstPlace] = copy;
      }
      return users;
    }

    /**
     * Method to get the main AnchorPane of this controller's fxml file
     *
     * @return the main AnchorPane of this controller's fxml file
     */
    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) leaderboardMainMenuButton.getParent();
    }

    public class LeaderboardRowData {
        private int rank;
        private String username;
        private int wins;
        private int losses;

        public LeaderboardRowData(int rank, String username, int wins, int losses) {
            this.rank = rank;
            this.username = username;
            this.wins = wins;
            this.losses = losses;
        }

        public int getRank() {
            return rank;
        }

        public String getUsername() {
            return username;
        }

        public int getWins() {
            return wins;
        }

        public int getLosses() {
            return losses;
        }
    }
}
