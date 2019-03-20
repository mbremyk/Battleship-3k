package game;

import database.Constants;
import database.DatabaseConnector;
import model.BattleshipUser;

/**
 * two boards
 * what players are playing
 * graphical logic
 */
public class Game {
	private BattleshipUser hostUser;
	private BattleshipUser joinUser;
	private Board board1;
	private Board board2;
	private DatabaseConnector databaseConnector;
	private String gameName;
	private boolean gameOpen = true; //open to join
	private int gameId;
	
	public Game(BattleshipUser hostUser) {
		databaseConnector = new DatabaseConnector(Constants.DB_URL);
		this.hostUser = hostUser;
		board1 = new Board(gameId);
		board2 = new Board(gameId);
	}

	public int getGameId() {
		return gameId;
	}
	public BattleshipUser getHostUser() {
		return hostUser;
	}
	public BattleshipUser getJoinUser() {
		return joinUser;
	}
	public void setJoinUser(BattleshipUser newUser){
		this.joinUser = newUser;
	}

	//whenever there is only one player (hosting game)
	public void waitForUser() { //waits for joinUser to join game

		//list game info in "join game window"
		//create thread
		//Poll database for user join
		//joinUser = databaseGetUser();
		gameOpen = false; //game full
	}

	//whenever two players are in the lobby
//	public void createGame() {
//
//	}

	/*public static void main(String[] args) {
        while (true) {
            if (DatabaseConnector.getTurn == 1) {
                player1.turn();
            }
            else if (DatabaseConnector.getTurn == 2) {
                player1.turn();
            }
        }
    }*/
}
