/**
 * Board.java
 * <p>
 * Loads a grid and calculates the tile positions of the cursor.
 * Has an ArrayList of ship objects, and can upload the positions of the ships via a DatabaseConnector
 * Uploads and downloads attacks to/from the database
 *
 * @author Thorkildsen Torje
 */

package game;

import database.DatabaseConnector;
import effects.DownScaler;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

import java.util.ArrayList;

public class Board extends ImageView {

    /**
     * Width and height of Board image
     */
    public static final int SIZE = 300;

    /**
     * Tiles in x and y direction/grid size
     */
    public static final int TILES = 10;

    /**
     * The width/height of each tile in the grid
     */
    public static final double TILE_SIZE = SIZE / ((double) TILES); //Tiles in x and y direction

    /**
     * The current x-position of the cursor in the Board's grid, -1 if outside
     */
    private int mousePosX = -1;

    /**
     * The current y-position of the cursor in the Board's grid, -1 if outside
     */
    private int mousePosY = -1;

    /**
     * The parent AnchorPane of this Board
     */
    private final AnchorPane parent;

    /**
     * The Board's number, 1 if local user and 2 if opponent
     */
    private final int boardNumber;

    /**
     * 2D-array with information about the tiles in the grid
     * <p>
     * -2 = ship, attacked and destroyed
     * -1 = no ship but tile attacked
     * 0 = no ship, not attacked
     * >0 = ship number n in the ArrayList ships, not attacked
     */
    private int[][] board;

    private ArrayList<Ship> ships = new ArrayList<Ship>();

    /**
     * Initiates a new Board object that can be used to display a grid
     *
     * @param parent      the parent AnchorPane of this object
     * @param x           the x position of this object
     * @param y           the y position of this object
     * @param boardNumber the number of this board, 1 = local user, 2 = opponent
     */
    public Board(AnchorPane parent, double x, double y, int boardNumber) {
        super(new Image("./grid10x10.png"));
        this.parent = parent;
        setTranslateX(x);
        setTranslateY(y);
        this.boardNumber = boardNumber;

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

    /**
     * constructor for testing purposes. Like the other constructor, but without anchorPane parent
     *
     * @param parent the parent AnchorPane of this object
     * @param x      the x position of this object
     * @param y      the y position of this object
     */
    public Board(AnchorPane parent, double x, double y) {
        this.parent = parent;
        setTranslateX(x);
        setTranslateY(y);
        this.boardNumber = 0;

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

    /**
     * Finds the mouse's position in this Board's grid, and stores it in the class variables mousePosX and mousePosY
     *
     * @param x the x-position of the mouse in the board, where x=0 is the upper left corner of the board
     * @param y the y-position of the mouse in the board, where y=0 is the upper left corner of the board
     */
    public void findMousePos(double x, double y) {
        mousePosX = (int) (x / TILE_SIZE);
        mousePosY = (int) (y / TILE_SIZE);
    }

    /**
     * Registers a ship in the class variable "board", which is a 2D-array
     * Sets grid-positions the ship occupy to the ship's index in the ship-array
     *
     * @param ship the ship that should be registered
     * @return coordinates in database-form string
     */
    public String registerShip(Ship ship) {
        String ret = "";
        int x = ship.getTileX();
        int y = ship.getTileY();
        int width = ship.getWidthTiles();
        int height = ship.getHeightTiles();
        int rotation = ship.getRotation();
        ret += x < 10 ? "0" + x : x;
        ret += "," + (y < 10 ? "0" + y : y);
        ret += "," + (width < 10 ? "0" + width : width);
        ret += "," + (height < 10 ? "0" + height : height);
        ret += "," + (rotation < 10 ? "00" + rotation : (rotation < 100 ? "0" + rotation : rotation));
        registerShipCoordinates(ship.getTileX(), ship.getTileY(), ship.getTilesX(), ship.getTilesY(), ships.indexOf(ship));
        return ret;
    }

    /**
     * Confirms the placements of the ships by adding them to the board.
     * Uploads their coordinates to the database if no ships are overlapping.
     *
     * @return an ArrayList of the ships that are overlapping, null if ship placements are valid
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
        databaseConnector.uploadShipCoordinates(shipCoordinates);
        return null;
    }

    /**
     * Checks if any ships are overlapping
     *
     * @return true if no ships are overlapping and false otherwise
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

    /**
     * Adds an Array of ships to an existing ArrayList
     *
     * @param original the original ArrayList that's getting more ships
     * @param extra    the new ships that will be added to the ArrayList
     */
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
     * @param ship1 ship number 1
     * @param ship2 ship number 2
     * @return true if the ships overlap and false if not
     */
    private Ship[] shipsOverlap(Ship ship1, Ship ship2) {
        int[] pos1 = ship1.getBasePosition();
        int width1 = ship1.getTilesX();
        int height1 = ship1.getTilesY();
        int[] pos2 = ship2.getBasePosition();
        int width2 = ship2.getTilesX();
        int height2 = ship2.getTilesY();

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
     * @param x         the ship's upper left corner's x-position in the grid
     * @param y         the ship's upper left corner's y-position in the grid
     * @param width     the width of the ship
     * @param height    the height of the ship
     * @param shipIndex the ship's index, which is its index in the ship ArrayList
     */
    public void registerShipCoordinates(int x, int y, int width, int height, int shipIndex) {
        for (int w = 0; w < width; w++) {
            for (int h = 0; h < height; h++) {
                board[x + w][y + h] = shipIndex + 1;
            }
        }
    }

    public void registerShipCoordinates(Ship ship) {
        registerShipCoordinates(ship.getTileX(), ship.getTileY(), ship.getTilesX(), ship.getTilesY(), ships.indexOf(ship));
    }

    /**
     * Adds a Ship object to the stage and registers it in the ships array
     *
     * @return true if ship could be added and false if there was a problem (for example spaces occupied)
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
        addShip(new Ship(visible, 2, 5, 4, 1, 0, this));
        addShip(new Ship(visible, 5, 1, 3, 2, 0, this));
        addShip(new Ship(visible, 8, 8, 2, 1, 0, this));
        addShip(new Ship(visible, 8, 3, 3, 1, 90, this));
        addShip(new Ship(visible, 0, 7, 2, 2, 0, this));
    }

    /**
     * Loads ships from a game in the database based on a gameid and userid (of the game's host)
     *
     * @param gameid the id of the game in the database
     * @param userid the id of the host user of the game in the database
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
//        System.out.println("Opponent board:\n" + this);
    }

    /**
     * Attacks a tile on the board's grid
     *
     * @param x the x-position on the grid
     * @param y the y-position on the grid
     * @return -1 if tile already attacked, 0 if no boats, and 1 if boat
     */
    public int attack(int x, int y) {
        return attack(x, y, true);
    }

    /**
     * Attacks a tile on the board's grid
     *
     * @param x      the x-position on the grid
     * @param y      the y-position on the grid
     * @param upload true if the attack should be uploaded to the database
     * @return -1 if tile already attacked, 0 if no boats, and 1 if boat
     */
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
     * @param x     the x-position on the grid
     * @param y     the y-position on the grid
     * @param color the color of the square, set to null if an image should be used
     * @param image the square's image, set to null if plain color
     */
    private void addTileColor(int x, int y, Color color, Image image) {
        Rectangle square = new Rectangle(Board.TILE_SIZE, Board.TILE_SIZE);
        square.setMouseTransparent(true);
        if (color != null) square.setFill(color);
        if (image != null) square.setFill(new ImagePattern(image));
        square.setTranslateX(this.getTranslateX() + x * Board.TILE_SIZE);
        square.setTranslateY(this.getTranslateY() + y * Board.TILE_SIZE);
        if (boardNumber == 1) {
            square.setOpacity(0.6);
            parent.getChildren().add(parent.getChildren().indexOf(this) + ships.size(), square);
        } else parent.getChildren().add(parent.getChildren().indexOf(this), square);
        DownScaler downScaler = new DownScaler(square);
        downScaler.play();
    }

    /**
     * Adds an attack to the upload cache
     * Will be uploaded later by the another thread
     *
     * @param x the x-position on the grid
     * @param y the y-position on the grid
     */
    private void uploadAttack(int x, int y) {
        String coordString = "";
        coordString += (x < 10) ? "0" + x : x;
        coordString += "," + ((y < 10) ? "0" + y : y);

        Statics.getGame().addUploadAction(coordString);
    }

    /**
     * Sets all ships either transparent or visible to mouse events
     *
     * @param transparent true if ships should be transparent to mouse events, false if they should be visible
     */
    public void setShipsMouseTransparent(boolean transparent) {
        for (Ship ship : ships) {
            ship.setMouseTransparent(transparent);
        }
    }

    /**
     * Checks how many ships are not destroyed (are alive) and returns the amount
     *
     * @return the amount of ships remaining
     */
    public int shipsRemaining() {
        if (ships.size() == 0) return -1; //If boards are not ready
        int count = 0;
        for (Ship ship : ships) {
            if (ship.isAlive()) count++;
        }
        return count;
    }

    /**
     * Gets the mouse's x-position in the grid
     *
     * @return the class variable mousePosX, the mouse's x-position in the grid
     */
    public int getMousePosX() {
        return mousePosX;
    }

    /**
     * Gets the mouse's y-position in the grid
     *
     * @return the class variable mousePosY, the mouse's y-position in the grid
     */
    public int getMousePosY() {
        return mousePosY;
    }

    /**
     * Converts the class variable "board" which is a 2D-array with information about ship placement and attacked
     * tiles to a String and returns it
     *
     * @return the 2D-array class variable "boards" as a String
     */
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
