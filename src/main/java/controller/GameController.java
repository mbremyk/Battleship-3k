package controller;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.DatabaseConnector;
import effects.Scaler;
import effects.Shaker;
import game.Board;
import game.MouseFollower;
import game.Ship;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
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
    Board board1;
    Board board2;
    int pressedBoard = -1;
    int pressedTileX = -1;
    int pressedTileY = -1;
    private static boolean shipsMovable = true;


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

        //THIS ORDER IS VERY IMPORTANT---------------------
        gameMainPane.getChildren().addAll(board1, board2);
        board1.addDefaultShips(true);
//        board2.addDefaultShips(false); //NOT NEEDED, LOADED FROM DATABASE AND ARE INVISIBLE ANYWAY
        gameMainPane.getChildren().add(mouseFollower);
        //-------------------------------------------------

        gameMainPane.setOnMouseMoved(event -> {
            moveMouseFollower(event.getX(), event.getY());
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
                    addTileColor(board2, attackX, attackY, Color.RED);
                } else if (attackResult == 0) {
//                    System.out.println("MISS!");
                    addTileColor(board2, attackX, attackY, Color.BLUE);
                }
            }
            moveMouseFollower(event.getX(), event.getY());
            colorMouseFollower(true);
        });

        gameReadyButton.setOnAction(event -> {
            ArrayList<Ship> overlappingShips = board1.registerShipCoordinates();
            if (overlappingShips == null) {
                gameReadyButton.setText("Waiting for opponent");
                gameReadyButton.setVisible(false);
                shipsMovable = false;
                mouseFollower.setVisible(true);
                DatabaseConnector databaseConnector = new DatabaseConnector();
                databaseConnector.uploadShipCoordinates(board1);
                board2.loadShipsFromDatabase(3, 6);
                System.out.println(board2);
            } else {
                //FOR SHAKING THE WHOLE SCENE
//                Shaker shaker = new Shaker(gameMainPane);
//                shaker.shake();

                for (Ship ship : overlappingShips){
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
    private void addTileColor(Board board, int x, int y, Color color) {
        Rectangle square = new Rectangle(Board.TILE_SIZE, Board.TILE_SIZE);
        square.setFill(color);
        square.setTranslateX(board.getTranslateX() + x * Board.TILE_SIZE);
        square.setTranslateY(board.getTranslateY() + y * Board.TILE_SIZE);
        gameMainPane.getChildren().add(gameMainPane.getChildren().indexOf(mouseFollower) - 1, square);
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
