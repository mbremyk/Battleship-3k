package game;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.util.Arrays;

/**
 * info on where objects are located on the board
 * attack logic
 */

public class Board extends ImageView {
    //private int boardNumber; //bord nr 1 eller 2 (korresponderer med spillernr
    int[][] board;


    public Board() {
//        super(new Image("./grid10x10.png"));
        board = new int[10][10];
    }

    public void saveShipPositions(int[][] pos) {
        for (int i = 0; i < pos.length; i++) {
            board[pos[i][0]][pos[i][1]] = 1;
        }
    }

    public boolean attack(int x, int y) {
        //checks if defenders board has a ship at given coordinates

        return false;
    }

    @Override
    public String toString() {
        String ret = "";

        for (int j = 0; j < board[0].length; j++) {
            ret += "|";
            for (int i = 0; i < board.length; i++) {
                ret += board[i][j] + ((i == board.length - 1) ? "" : ",");
            }
            ret += "|\n";
        }

        return ret;
    }

    /**
     * Test client
     * @param args
     */

    public static void main(String[] args){
        Board board = new Board();
        int[][] testPos = {
                {0,4},
                {1,4},
                {2,4},
                {6,6},
                {6,7},
        };
        board.saveShipPositions(testPos);
        System.out.println(board);
    }
}
