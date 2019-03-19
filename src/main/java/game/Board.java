package game;

import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;

import java.util.ArrayList;

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
    private final AnchorPane parent;

    //private int boardNumber; //bord nr 1 eller 2 (korresponderer med spillernr
    private int[][] board;
    private ArrayList<Ship> ships = new ArrayList<Ship>();


    public Board(int boardNumber, AnchorPane parent, double x, double y) {
        super(new Image("./grid10x10.png"));
        this.boardNumber = boardNumber;
        this.parent = parent;
        setTranslateX(x);
        setTranslateY(y);

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


    public void saveShipPosition(Ship ship) {
        int[] pos = ship.getBasePosition();
        int[][] size = ship.getSize();
        for (int i = 0; i < size.length; i++) {
            for (int j = 0; j < size[0].length; j++) {
                board[pos[0] + i][pos[1] + j] = 1;
            }
        }
    }

    /**
     * Confirms the placements of the ships by adding them to the board
     */
    public void registerShipCoordinates() {
        for (Ship ship : ships) {
            saveShipPosition(ship);
        }
        System.out.println("Registered ships:\n" + toString());
        System.out.println("In database coordinates:\n"+getShipsForDatabase());
    }

    /**
     * Adds a Ship object to the stage and registers it in the ships array
     *
     * @return boolean, true if ship could be added and false if there was a problem (for example spaces occupied)
     */
    public boolean addShip(Ship ship) {
        if (this.parent == null) return false;
        this.parent.getChildren().add(ship);
        ships.add(ship);
        return true;
    }

    /**
     * @param visible, sets the visibility of the boat (might not be needed in future versions
     * @// TODO: 19.03.2019  remove visible boolean if enemy boats aren't needed
     */

    public void addDefaultShips(boolean visible) {
        addShip(new Ship(visible, 2, 5, 5, 1, this));
        addShip(new Ship(visible, 5, 1, 3, 2, this));
        addShip(new Ship(visible, 8, 8, 2, 1, this));
        addShip(new Ship(visible, 8, 3, 1, 3, this));
        addShip(new Ship(visible, 0, 7, 2, 2, this));
    }

    /**
     * Gets the occupied tiles as a String written as xx,yy,xx,yy,xx,yy...
     * Used in the battleship_board table in the database for storing ship coordinates
     *
     * @return String with coordinates written as xx,yy,xx,yy,xx,yy...
     */
    public String getShipsForDatabase() {
        String ret = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                if (board[i][j] == 1) {
                    ret += i < 10 ? "0" + i : i;
                    ret += "," + (j < 10 ? "0" + j : j)+",";
                }
            }
        }
        ret = ret.substring(0,ret.length()-1);
        return ret;
    }

    public boolean attack(int x, int y) {
        //checks if defenders board has a ship at given coordinates

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
        Board board = new Board(1, null, 0, 0);
        Ship ship = new Ship(false, 2, 5, 5, 2, new Board(10, new AnchorPane(), 0, 0));
        board.saveShipPosition(ship);
        System.out.println(board);
    }
}
