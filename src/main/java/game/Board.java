package game;
import database.DatabaseConnector;
import javafx.scene.image.ImageView;
import model.BattleshipUser;

import javax.xml.crypto.Data;

import static database.Constants.*;

/**
 * info on where objects are located on the board
 * attack logic
 */

public class Board extends ImageView {
    //private int boardNumber; //bord nr 1 eller 2 (korresponderer med spillernr
    int[][] board;
    private final int gameId;


    public Board(int gameId){
        super();
        board = new int[10][10];
        this.gameId = gameId;
    }



    public boolean attack(int x, int y) {
        //checks if defenders board has a ship at given coordinates
        DatabaseConnector databaseConnector2 = new DatabaseConnector(DB_URL);
        if (databaseConnector2.doAction(Statics.getGame().getGameId(), x, y)) {
            return true;
        }
        return false;
    }
}
