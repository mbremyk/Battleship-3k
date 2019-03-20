/**
 * Board.java
 * info on where objects are located on the board
 * attack logic
 * @Author Grande Trym
 */
package game;
import database.DatabaseConnector;
import javafx.scene.image.ImageView;
import model.BattleshipUser;

import javax.xml.crypto.Data;

import static database.Constants.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 * info on where objects are located on the board
 * attack logic
 */

public class Board extends ImageView {
    public static final int SIZE = 300; //Width and height of image
    public static final int TILES = 10; //Tiles in x and y direction
    public static final double TILE_SIZE = SIZE / ((double) TILES); //Tiles in x and y direction

    private final int boardNumber;
    private int mousePosX = -1;
    private int mousePosY = -1;

    //private int boardNumber; //bord nr 1 eller 2 (korresponderer med spillernr
    int[][] board;


    public Board(int boardNumber) {
        super(new Image("./grid10x10.png"));
        this.boardNumber = boardNumber;
        board = new int[TILES][TILES];
        this.setFitWidth(SIZE);
        this.setFitHeight(SIZE);

        setOnMouseMoved(event -> {
            findMousePos(event.getX(), event.getY());
        });
        setOnMouseExited(event -> {
            mousePosX = -1;
            mousePosY = -1;
        });
        setOnMouseClicked(event -> {
            //Attack with mousePosX and mousePosY
            if (boardNumber == 1) System.out.println("Placing boat on " + mousePosX + "," + mousePosY);
            if (boardNumber == 2) System.out.println("Attacking " + mousePosX + "," + mousePosY);
        });
    }

    public void findMousePos(double x, double y) {
        mousePosX = (int) (x / TILE_SIZE);
        mousePosY = (int) (y / TILE_SIZE);
    }


    public void saveShipPositions(int[][] pos) {
        for (int i = 0; i < pos.length; i++) {
            board[pos[i][0]][pos[i][1]] = 1;
        }
    }



    public boolean attack(int x, int y) {
        //checks if defenders board has a ship at given coordinates
        DatabaseConnector databaseConnector2 = new DatabaseConnector(DB_URL);
        if (databaseConnector2.doAction(Statics.getGame().getGameId(), x, y)) {
            return true;
        }
        return false;
    }

    public int getMousePosX() {
        return mousePosX;
    }

    public int getMousePosY() {
        return mousePosY;
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
     *
     * @param args
     */

    public static void main(String[] args) {
        //Disable/comment out super(new Image("./grid10x10.png")); in constructor to test
        Board board = new Board(1);
        int[][] testPos = {
                {0, 4},
                {1, 4},
                {2, 4},
                {6, 6},
                {6, 7},
        };
        board.saveShipPositions(testPos);
        System.out.println(board);
    }
}
