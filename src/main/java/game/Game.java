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
	
	Game(BattleshipUser user1, BattleshipUser user2) {
		this.user1 = user1;
		this.user2 = user2;
		board1 = new Board();
		board2 = new Board();
		databaseConnector = new DatabaseConnector(Constants.DB_URL);
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
