package game;
import model.BattleshipUser;

/**
 * two boards
 * which players are playing
 * graphical logic
 * communication with controller(game looks), dbconstants, battleshipuser, dbc
 */
public class Game {
    private BattleshipUser player1;
    private BattleshipUser player2;
    private Board board1 = new Board(1);
    private Board board2 = new Board(2);

    Game(BattleshipUser player1, BattleshipUser player2) {
        this.player1 = player1;
        this.player2 = player2;
    }





}