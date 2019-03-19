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
	private BattleshipUser user1;
	private BattleshipUser user2;
	private Board board1;
	private Board board2;
	private DatabaseConnector databaseConnector;
	private String gameName;
	private BattleshipUser hostUser;
	private boolean gameOpen = false;
	
	Game(BattleshipUser user1, BattleshipUser user2) {
		this.user1 = user1;
		this.user2 = user2;
		board1 = new Board();
		board2 = new Board();
		databaseConnector = new DatabaseConnector(Constants.DB_URL);
	}

	//whenever there is only one player (hosting game)
	public void waitForUser() { //waits for user2 to join game lobby

		//list game info in "join game window"

		//let user2 join lobby
		//Poll database for user join
		//user2 = databaseGetUser();
		//createGame(user1, user);
	}

	//whenever two players are in the lobby
	public void createGame() {
		gameOpen = true;
		databaseConnector.createGame(new Game(user1, user2));
	}

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
