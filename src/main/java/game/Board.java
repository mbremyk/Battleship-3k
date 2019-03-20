package game;

import database.DatabaseConnector;
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

    private int mousePosX = -1;
    private int mousePosY = -1;
    private final AnchorPane parent;

    private int[][] board;
    /*
    -1 = no ship but tile attacked
    0 = no ship, not attacked
    1 = ship, not attacked
    2 = ship, attacked and destroyed
     */

    private ArrayList<Ship> ships = new ArrayList<Ship>();


    public Board(AnchorPane parent, double x, double y) {
        super(new Image("./grid10x10.png"));
        this.parent = parent;
        setTranslateX(x);
        setTranslateY(y);

        board = new int[TILES][TILES];
        this.setFitWidth(SIZE);
        this.setFitHeight(SIZE);

        setOnMouseMoved(event -> {
            findMousePos(event.getX(), event.getY());
        });
        setOnMouseDragged(event -> {
            if (event.getX() > 0 && event.getY() > 0) {
                findMousePos(event.getX(), event.getY());
            }
        });
        setOnMouseExited(event -> {
            mousePosX = -1;
            mousePosY = -1;
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
        System.out.println("In database coordinates:\n" + getShipsForDatabase());
    }

    /**
     * Registers ship coordinates from an int[][], like the coordinates DatabaseConnector.java gives you
     *
     * @param coords
     */
    public void registerShipCoordinates(int[][] coords) {
        for (int i = 0; i < coords.length; i++) {
            board[coords[i][0]][coords[i][1]] = 1;
        }
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
                    ret += "," + (j < 10 ? "0" + j : j) + ",";
                }
            }
        }
        ret = ret.substring(0, ret.length() - 1);
        return ret;
    }

    /**
     * @param gameid
     * @param userid
     */
    public void loadShipsFromDatabase(int gameid, int userid) {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        int[][] coords = databaseConnector.getShipCoordinates(gameid, userid);
        registerShipCoordinates(coords);
    }

    /**
     * Attacks a spot on the board
     * @param x
     * @param y
     * @return int -1 if tile already attacked, 0 if no boats, and 1 if boat
     */
    public int attack(int x, int y) {
        switch (board[x][y]){
            case -1:
                return -1;
            case 0:
                board[x][y] = -1;
                return 0;
            case 1:
                board[x][y] = 2;
                return 1;
            case 2:
                return -1;
        }
        return -1;
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
        Board board = new Board(null, 0, 0);
        Ship ship = new Ship(false, 2, 5, 5, 2, new Board(new AnchorPane(), 0, 0));
        board.saveShipPosition(ship);
        System.out.println(board);
    }
}
