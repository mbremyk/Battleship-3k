package controller;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.DatabaseConnector;
import database.PullThread;
import effects.DownScaler;
import effects.Scaler;
import effects.Shaker;
import game.Board;
import game.MouseFollower;
import game.Ship;
import game.Statics;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

public class GameController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane gameToolsPane;

    @FXML
    private Text gameOpponentNameText;

    @FXML
    private AnchorPane gameAttacksPane;

    @FXML
    private Text gameGameNameText;

    @FXML
    private JFXButton gameReadyButton;

    @FXML
    private Text gameUserNameText;

    @FXML
    private AnchorPane gameMainPane;

    @FXML
    private ImageView gameOptionsImage;

    private MouseFollower mouseFollower;
    private Board board1;
    private Board board2;
    private int pressedBoard = -1;
    private int pressedTileX = -1;
    private int pressedTileY = -1;
    private static boolean shipsMovable = true;

    private int boardsReady = 0; //1 if ready, 2 if update fixed


    @FXML
    void initialize() {
        assert gameToolsPane != null : "fx:id=\"gameToolsPane\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameOpponentNameText != null : "fx:id=\"gameOpponentNameText\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameAttacksPane != null : "fx:id=\"gameAttacksPane\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameGameNameText != null : "fx:id=\"gameGameNameText\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameReadyButton != null : "fx:id=\"gameReadyButton\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameUserNameText != null : "fx:id=\"gameUserNameText\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameMainPane != null : "fx:id=\"gameMainPane\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameOptionsImage != null : "fx:id=\"gameOptionsImage\" was not injected: check your FXML file 'Game.fxml'.";

        shipsMovable = true;
        addUIComponents();
    }

    /**
     * Adds the Boards and the MouseFollower.
     */
    public void addUIComponents() {
        mouseFollower = new MouseFollower();
        mouseFollower.setVisible(false);
        board1 = new Board(gameMainPane, 250, 200);
        board2 = new Board(gameMainPane, 650, 200);
        Statics.getGame().setBoards(board1, board2);

        //THIS ORDER IS VERY IMPORTANT---------------------
        gameMainPane.getChildren().addAll(board1, board2);
        board1.addDefaultShips(true);
        //Wait thread
        PullThread pullThread = new PullThread(this);
        pullThread.start();
        board2.setShipsMouseTransparent(true);
        gameMainPane.getChildren().add(mouseFollower);
        //-------------------------------------------------

        gameMainPane.setOnMouseMoved(event -> {
            moveMouseFollower(event.getX(), event.getY());
            if (boardsReady == 1) {
                updateBoards();
            }
        });
        gameMainPane.setOnMouseDragged(event -> {
            colorMouseFollower();
        });

        gameMainPane.setOnMousePressed(event -> {
            //Attack with mousePosX and mousePosY
            pressedBoard = getMouseBoardNumber();
            if (pressedBoard == 1) {
                pressedTileX = board1.getMousePosX();
                pressedTileY = board1.getMousePosY();
            } else if (pressedBoard == 2) {
                pressedTileX = board2.getMousePosX();
                pressedTileY = board2.getMousePosY();
            }
            colorMouseFollower();
        });
        gameMainPane.setOnMouseReleased(event -> {
            //Attack with mousePosX and mousePosY
            int boardNumber = onSameTiles();
            if (boardNumber == 1) {
                System.out.println("Placing boat on " + board1.getMousePosX() + "," + board1.getMousePosY());
            } else if (boardNumber == 2) {
                int attackX = board2.getMousePosX();
                int attackY = board2.getMousePosY();
                int attackResult = board2.attack(attackX, attackY);
                if (attackResult == 1) {
//                    System.out.println("HIT!");
//                    addTileColor(board2, attackX, attackY, Color.RED);
                    addTileColor(board2, attackX, attackY, null, new Image("./ExplosionTile.png"));
                } else if (attackResult == 0) {
//                    System.out.println("MISS!");
//                    addTileColor(board2, attackX, attackY, Color.BLUE);
                    addTileColor(board2, attackX, attackY, null, new Image("./WaterTile.png"));
                }
            }
            moveMouseFollower(event.getX(), event.getY());
            colorMouseFollower(true);
        });

        gameReadyButton.setOnAction(event -> {
            ArrayList<Ship> overlappingShips = board1.uploadShipCoordinates();
            if (overlappingShips == null) {
                //If no ships are overlapping (the ships have been uploaded)
                board1.setShipsMouseTransparent(true);
                gameReadyButton.setText("Waiting for opponent");
                gameReadyButton.setVisible(false);
                shipsMovable = false;
                mouseFollower.setVisible(true);
            } else {
                //FOR SHAKING THE WHOLE SCENE
//                Shaker shaker = new Shaker(gameMainPane);
//                shaker.shake();

                for (Ship ship : overlappingShips) {
//                    Shaker shaker = new Shaker(ship);
//                    shaker.shake();
                    Scaler scaler = new Scaler(ship);
                    scaler.play();
                }
            }
        });
    }

    private int getMouseBoardNumber() {
        if (board1.getMousePosX() != -1 && board1.getMousePosY() != -1) {
            return 1;
        } else if (board2.getMousePosX() != -1 && board2.getMousePosY() != -1) {
            return 2;
        }
        return -1;
    }

    /**
     * Adds a square to a board that indicates if an attack has missed or hit
     *
     * @param board
     * @param x
     * @param y
     */
    private void addTileColor(Board board, int x, int y, Color color, Image image) {
        Rectangle square = new Rectangle(Board.TILE_SIZE, Board.TILE_SIZE);
        square.setMouseTransparent(true);
        if (color != null) square.setFill(color);
        if (image != null) square.setFill(new ImagePattern(image));
        square.setTranslateX(board.getTranslateX() + x * Board.TILE_SIZE);
        square.setTranslateY(board.getTranslateY() + y * Board.TILE_SIZE);
//        gameMainPane.getChildren().add(gameMainPane.getChildren().indexOf(mouseFollower), square);
        gameMainPane.getChildren().add(gameMainPane.getChildren().indexOf(board2), square);
        DownScaler downScaler = new DownScaler(square);
        downScaler.play();
    }

    //returns board number if cursor is on same tiles as when pressed
    private int onSameTiles() {
        if (pressedBoard == getMouseBoardNumber() && !shipsMovable && pressedBoard != -1 && pressedTileX != -1 && pressedTileY != -1) {
            if (board1.getMousePosX() == pressedTileX && board1.getMousePosY() == pressedTileY) {
                return 1;
            } else if (board2.getMousePosX() == pressedTileX && board2.getMousePosY() == pressedTileY) {
                return 2;
            }
        }
        return -1;
    }

    private void moveMouseFollower(double eventX, double eventY) {
        int boardNumber = getMouseBoardNumber();
        if (boardNumber == 1) {
            mouseFollower.setTilePos(board1.getTranslateX(), board1.getTranslateY(), board1.getMousePosX(), board1.getMousePosY());
        } else if (boardNumber == 2) {
            mouseFollower.setTilePos(board2.getTranslateX(), board2.getTranslateY(), board2.getMousePosX(), board2.getMousePosY());
        } else {
            mouseFollower.setPos(eventX, eventY);
        }
    }

    /**
     * Called by thread to signal that both boards are uploaded
     */
    public void boardsReady() {
        boardsReady = 1;
    }

    private void updateBoards() {
        System.out.println("boardsReady called");
        if (Statics.getGame().getJoinUser() != null && Statics.getGame().getHostUser() != null) {
            int opponentid;
            if (Statics.getLocalUser().equals(Statics.getGame().getHostUser()))
                opponentid = Statics.getGame().getJoinUser().getUserId();
            else opponentid = Statics.getGame().getHostUser().getUserId();
            board2.loadShipsFromDatabase(Statics.getGame().getGameId(), opponentid);
            boardsReady = 2;
            System.out.println("Opponent board:\n" + board2);
        }
    }

    private void colorMouseFollower() {
        colorMouseFollower(false);
    }

    private void colorMouseFollower(boolean removeColor) {
        if (onSameTiles() == -1 || removeColor) {
            mouseFollower.pressed(false);
        } else {
            mouseFollower.pressed(true);
        }
    }

    public static boolean isShipsMovable() {
        return shipsMovable;
    }
}
