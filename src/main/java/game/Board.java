package game;

import database.DatabaseConnector;
import model.BattleshipUser;

/**
 * info on where objects are located on the board
 * attack logic
 */

public class Board {
    //private int boardNumber; //bord nr 1 eller 2 (korresponderer med spillernr
    int[][] board;


    Board(){
        board = new int[10][10];
    }

    public boolean attack(int x, int y) {
        //checks if defenders board has a ship at given coordinates

        
    }
}
