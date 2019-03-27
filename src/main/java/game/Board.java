/**
 * Board.java
 *
 * @author
 */

package game;

import database.DatabaseConnector;
import effects.DownScaler;
import javafx.geometry.Point2D;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

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
    -2 = ship, attacked and destroyed
    -1 = no ship but tile attacked
    0 = no ship, not attacked
    n>0 = ship number n in the ArrayList ships, not attacked
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

    /**
     * @param ship
     * @return String in database-form coordinates
     */
    public String registerShip(Ship ship) {
        String ret = "";
        int[] pos = ship.getBasePosition();
        int x = pos[0];
        int y = pos[1];
        int width = ship.getWidthTiles();
        int height = ship.getHeightTiles();
        int rotation = ship.getRotation();
        ret += x < 10 ? "0" + x : x;
        ret += "," + (y < 10 ? "0" + y : y);
        ret += "," + (width < 10 ? "0" + width : width);
        ret += "," + (height < 10 ? "0" + height : height);
        ret += "," + (rotation < 10 ? "00" + rotation : (rotation < 100 ? "0" + rotation : rotation));
        registerShipCoordinates(x, y, width, height, ships.indexOf(ship));
        return ret;
    }

    /**
     * Confirms the placements of the ships by adding them to the board
     */
    public ArrayList<Ship> uploadShipCoordinates() {
        ArrayList<Ship> overlappingShips = checkNoShipsOverlap();
        if (overlappingShips != null) return overlappingShips;

        String shipCoordinates = "";
        for (Ship ship : ships) {
            shipCoordinates += registerShip(ship) + "%";
        }
        shipCoordinates = shipCoordinates.substring(0, shipCoordinates.length() - 1);

        DatabaseConnector databaseConnector = new DatabaseConnector();
//        System.out.println("Registered ships:\n" + toString());
//        System.out.println("In database coordinates:\n" + shipCoordinates);
        boolean uploadStatus = databaseConnector.uploadShipCoordinates(shipCoordinates);
//        System.out.println("Board uploaded: " + uploadStatus);
        return null;
    }

    /**
     * Checks if any ships are overlapping
     *
     * @return boolean, true if no ships are overlapping and false otherwise
     */
    private ArrayList<Ship> checkNoShipsOverlap() {
        ArrayList<Ship> overlappingShips = null;
        for (int i = 0; i < ships.size() - 1; i++) {
            Ship ship1 = ships.get(i);
            for (int j = i + 1; j < ships.size(); j++) {
                Ship ship2 = ships.get(j);
                Ship[] extraOverlappingShips = shipsOverlap(ship1, ship2);
                if (extraOverlappingShips != null) {
                    if (overlappingShips == null) {
                        overlappingShips = new ArrayList<>();
                    }
                    addToShipArray(overlappingShips, extraOverlappingShips);
                }
            }
        }

        return overlappingShips;
    }

    private void addToShipArray(ArrayList original, Ship[] extra) {
        for (Ship ship : extra) {
            if (original.indexOf(ship) == -1) {
                original.add(ship);
            }
        }
    }

    /**
     * Checks if two ships are overlapping on the board
     *
     * @param ship1
     * @param ship2
     * @return boolean, true if the ships overlap and false if not
     */
    private Ship[] shipsOverlap(Ship ship1, Ship ship2) {
        int[] pos1 = ship1.getBasePosition();
        int width1 = ship1.getWidthTiles();
        int height1 = ship1.getHeightTiles();
        int[] pos2 = ship2.getBasePosition();
        int width2 = ship2.getWidthTiles();
        int height2 = ship2.getHeightTiles();

        if (pos1[0] + width1 - 1 < pos2[0]
                || pos2[0] + width2 - 1 < pos1[0]
                || pos1[1] + height1 - 1 < pos2[1]
                || pos2[1] + height2 - 1 < pos1[1]) {
            return null;
        }
        return new Ship[]{ship1, ship2};
    }

    /**
     * Registers ship coordinates from a base x and y, width and height, like the coordinates DatabaseConnector.java gives you
     *
     * @param x
     * @param y
     * @param width
     * @param height
     * @param shipIndex
     */
    public void registerShipCoordinates(int x, int y, int width, int height, int shipIndex) {
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                board[x + w][y + h] = shipIndex + 1;
            }
        }
    }

    public void registerShipCoordinates(Ship ship) {
        registerShipCoordinates(ship.getTileX(), ship.getTileY(), ship.getWidthTiles(), ship.getHeightTiles(), ships.indexOf(ship));
    }

    /**
     * Adds a Ship object to the stage and registers it in the ships array
     *
     * @return boolean, true if ship could be added and false if there was a problem (for example spaces occupied)
     */
    public boolean addShip(Ship ship) {
        if (this.parent == null) return false;
        this.parent.getChildren().add(parent.getChildren().indexOf(this) + 1, ship);
        ships.add(ship);
        return true;
    }

    /**
     * @param visible, sets the visibility of the boat (might not be needed in future versions
     */

    public void addDefaultShips(boolean visible) {
        addShip(new Ship(visible, 2, 5, 5, 1, 0, this));
        addShip(new Ship(visible, 5, 1, 3, 2, 0, this));
        addShip(new Ship(visible, 8, 8, 2, 1, 0, this));
        addShip(new Ship(visible, 8, 3, 1, 3, 90, this));
        addShip(new Ship(visible, 0, 7, 2, 2, 0, this));
    }

    /**
     * @param gameid
     * @param userid
     */
    public void loadShipsFromDatabase(int gameid, int userid) {
        DatabaseConnector databaseConnector = new DatabaseConnector();
        String coordString = databaseConnector.getShipCoordinatesString(gameid, userid);

        String[] coordArray = coordString.split("%");
        for (String shipCoordString : coordArray) {
            String[] shipCoordArray = shipCoordString.split(",");

            int x = Integer.parseInt(shipCoordArray[0]);
            int y = Integer.parseInt(shipCoordArray[1]);
            int width = Integer.parseInt(shipCoordArray[2]);
            int height = Integer.parseInt(shipCoordArray[3]);
            int rotation = Integer.parseInt(shipCoordArray[4]);

            Ship loadedShip = new Ship(false, x, y, width, height, rotation, this);

            addShip(loadedShip);
            registerShipCoordinates(loadedShip);
            setShipsMouseTransparent(true);
        }
        System.out.println("Opponent board:\n" + this);
    }

    /**
     * Attacks a spot on the board
     *
     * @param x
     * @param y
     * @return int -1 if tile already attacked, 0 if no boats, and 1 if boat
     */
    public int attack(int x, int y) {
        return attack(x, y, true);
    }

    public int attack(int x, int y, boolean upload) {
        Game game = Statics.getGame();
        if (!game.isBoardsReady() || game.isGameOver() && !upload) return -1;
        switch (board[x][y]) {
            case -2:
                return -1;
            case -1:
                return -1;
            case 0:
                board[x][y] = -1;
                if (upload) {
                    game.setMyTurn(false);
                    uploadAttack(x, y);
                } else {
                    game.incMoveID();
                }
                addTileColor(x, y, null, new Image("./WaterTile.png"));
                return 0;
            default:
                ships.get(board[x][y] - 1).reduceHealth();
                board[x][y] = -2;
                if (upload) {
                    uploadAttack(x, y);
                } else {
                    game.incMoveID();
                }
                addTileColor(x, y, null, new Image("./ExplosionTile.png"));
                return 1; //HIT
        }
    }

    /**
     * Adds a square to a board that indicates if an attack has missed or hit
     *
     * @param x
     * @param y
     * @param color
     * @param image
     */
    private void addTileColor(int x, int y, Color color, Image image) {
        Rectangle square = new Rectangle(Board.TILE_SIZE, Board.TILE_SIZE);
        square.setMouseTransparent(true);
        if (color != null) square.setFill(color);
        if (image != null) square.setFill(new ImagePattern(image));
        square.setTranslateX(this.getTranslateX() + x * Board.TILE_SIZE);
        square.setTranslateY(this.getTranslateY() + y * Board.TILE_SIZE);
        parent.getChildren().add(parent.getChildren().indexOf(this), square);
        DownScaler downScaler = new DownScaler(square);
        downScaler.play();
    }

    private void uploadAttack(int x, int y) {
        String coordString = "";
        coordString += (x < 10) ? "0" + x : x;
        coordString += "," + ((y < 10) ? "0" + y : y);

//        DatabaseConnector db = new DatabaseConnector();
//        db.doAction(coordString);
        Statics.getGame().addUploadAction(coordString);
    }

    public void setShipsMouseTransparent(boolean transparent) {
        for (Ship ship : ships) {
            ship.setMouseTransparent(transparent);
        }
    }

    public int shipsRemaining() {
        if (ships.size() == 0) return -1; //If boards are not ready
        int count = 0;
        for (Ship ship : ships) {
            if (ship.isAlive()) count++;
        }
        return count;
    }

    public int getMousePosX() {
        return mousePosX;
    }

    public int getMousePosY() {
        return mousePosY;
    }

    public int[][] getBoard() {
        return board;
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
}
