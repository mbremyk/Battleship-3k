package game;
import model.BattleshipUser;

/**
 * two boards
 * which players are playing
 * graphical logic
 * communication with controller(game looks), dbconstants, battleshipuser, dbc
 */
public class Game {
    private Player player1;
    private Player player2;


    Game(BattleshipUser user1, BattleshipUser user2) {
        player1 = new Player(user1, 1);
        player2 = new Player(user2, 2);
    }

    public static void main(String[] args) {
        while (true) {
            if (getTurn == 1) {
                player1.turn();
            }
            else if (getTurn ==2) {
                player1.turn();
            }
        }

    }




}