/**
 * Game.java
 * @author Grande Trym
 */

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
//	private DatabaseConnector databaseConnector;
	private String gameName;
	private boolean gameOpen = true; //open to join
	private int gameId;
	
	public Game(int gameid, BattleshipUser hostUser) {
//		databaseConnector = new DatabaseConnector(Constants.DB_URL);
		this.gameId = gameid;
		this.hostUser = hostUser;
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
	public void move(String coordinates){}
	public boolean getGameState(){return false;}
	public int getMoveId(){return 0;}
	public void userJoined(){}

	// mOtherfuckers be making method for gameover and last move id, and incomming move

	public void setBoards(Board board1, Board board2){
		this.board1 = board1;
		this.board2 = board2;
	}

	public boolean isGameOpen() {
		return gameOpen;
	}

	public void setGameOpen(boolean gameOpen) {
		this.gameOpen = gameOpen;
	}

	@Override
	public String toString() {
		return "Game{" +
				"gameId=" + gameId +
				", hostUser=" + hostUser +
				", joinUser=" + joinUser +
				", board1=" + board1 +
				", board2=" + board2 +
//				", databaseConnector=" + databaseConnector +
				", gameName='" + gameName + '\'' +
				", gameOpen=" + gameOpen +
				'}';
	}
}
