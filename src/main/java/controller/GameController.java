/**
 * The controller for all game related objects.
 *
 * @Author Thorkildsen Torje
 */
package controller;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import database.DatabaseConnector;
import database.PullThread;
import effects.Scaler;
import game.*;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.BattleshipUser;

public class GameController extends ViewComponent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Text gameOpponentNameText;

    @FXML
    private Text gameGameNameText;

    @FXML
    private JFXButton gameReadyButton;

    @FXML
    private Text gameUserNameText;

    @FXML
    private AnchorPane gameMainPane;


    private MouseFollower mouseFollower;
    private Board board1;
    private Board board2;
    private Rectangle board1Shadow;
    private Rectangle board2Shadow;
    private int pressedBoard = -1;
    private int pressedTileX = -1;
    private int pressedTileY = -1;
    private boolean gameReady = false;
    private int boardsReady = 0; //1 if ready, 2 if update fixed


    @FXML
    void initialize() {
        assert gameOpponentNameText != null : "fx:id=\"gameOpponentNameText\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameGameNameText != null : "fx:id=\"gameGameNameText\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameReadyButton != null : "fx:id=\"gameReadyButton\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameUserNameText != null : "fx:id=\"gameUserNameText\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameMainPane != null : "fx:id=\"gameMainPane\" was not injected: check your FXML file 'Game.fxml'.";
        addUIComponents();
        updateText();
    }

    /**
     * Adds the Boards, the MouseFollower and all the other UI components used in the game and aren't in the fxml file.
     * Adds some event-listeners to the added components to make the game interactive
     */
    private void addUIComponents() {
        mouseFollower = new MouseFollower();
        mouseFollower.setVisible(false);
        board1 = new Board(gameMainPane, 50, 200);
        board2 = new Board(gameMainPane, 450, 200);
        board1Shadow = new Rectangle(board1.getTranslateX(), board1.getTranslateY(), board1.getFitWidth(), board1.getFitHeight());
        board2Shadow = new Rectangle(board2.getTranslateX(), board2.getTranslateY(), board2.getFitWidth(), board2.getFitHeight());
        board1Shadow.setOpacity(0.5);
        board2Shadow.setOpacity(0.5);
        board1Shadow.setVisible(false);
        board2Shadow.setVisible(false);
        board1Shadow.setMouseTransparent(true);
        board2Shadow.setMouseTransparent(true);
        Statics.getGame().setBoards(board1, board2);

        //THIS ORDER IS VERY IMPORTANT---------------------
        gameMainPane.getChildren().addAll(board1, board2, board1Shadow, board2Shadow);
        board1.addDefaultShips(true);
        //Wait thread
        Statics.setPullThread(new PullThread(this));
        Statics.getPullThread().start();
        board2.setShipsMouseTransparent(true);
        gameMainPane.getChildren().add(mouseFollower);
        //-------------------------------------------------

        gameMainPane.setOnMouseMoved(event -> {
            moveMouseFollower(event.getX(), event.getY());
        });
        gameMainPane.setOnMouseDragged(event -> {
            colorMouseFollower();
        });

        gameMainPane.setOnMousePressed(event -> {
           savePressedTile();
        });

        gameMainPane.setOnMouseReleased(event -> {
            attack();
            moveMouseFollower(event.getX(), event.getY());
            colorMouseFollower(true);
        });

        gameReadyButton.setOnAction(event -> {
            readyGame();
        });
    }

    /**
     * Stores the x and y position of the tile that was pressed in the object variables pressedTileX and pressedTileY
     */
    private void savePressedTile(){
        pressedBoard = getMouseBoardNumber();
        if (pressedBoard == 1) {
            pressedTileX = board1.getMousePosX();
            pressedTileY = board1.getMousePosY();
        } else if (pressedBoard == 2) {
            pressedTileX = board2.getMousePosX();
            pressedTileY = board2.getMousePosY();
        }
        colorMouseFollower();
    }

    /**
     * Checks if the mouse was released on the same tile as it was pressed, and attacks that tile
     * if it was pressed on the opponent's board while it is your turn.
     *
     * @return true if an attack was executed, false if not
     */
    private boolean attack(){
        int boardNumber = onSameTiles();
        if (boardNumber == 1) {
            //Could be used in the future
            return false;
        } else if (boardNumber == 2 && Statics.getGame().isMyTurn()) {
            int attackX = board2.getMousePosX();
            int attackY = board2.getMousePosY();
            board2.attack(attackX, attackY);
            return true;
        }
        return false;
    }

    /**
     * Gets the board number the cursor is overlapping with, either 1 or 2, or -1 if the cursor is outside of the boards.
     *
     * @return the board number the cursor overlaps with. -1 if none
     */
    private int getMouseBoardNumber() {
        if (board1.getMousePosX() != -1 && board1.getMousePosY() != -1) {
            return 1;
        } else if (board2.getMousePosX() != -1 && board2.getMousePosY() != -1) {
            return 2;
        }
        return -1;
    }

    /**
     * Checks if the mouse was released on the same tile as it was pressed
     * Used to make sure the mouse click did not move outside of the tile
     *
     * @return true if cursor is released on the same tile as when pressed, false if not
     */
    private int onSameTiles() {
        if (pressedBoard == getMouseBoardNumber() && !Statics.getGame().isShipsMovable() && pressedBoard != -1 && pressedTileX != -1 && pressedTileY != -1) {
            if (board1.getMousePosX() == pressedTileX && board1.getMousePosY() == pressedTileY) {
                return 1;
            } else if (board2.getMousePosX() == pressedTileX && board2.getMousePosY() == pressedTileY) {
                return 2;
            }
        }
        return -1;
    }

    /**
     * Method that tests if no ships are overlapping and uploads their positions to the database.
     * Stops ships from being movable, and does some minor graphics changes to indicate that your game is ready
     * and it waiting for an opponent.
     * Also starts a timer that runs every frame and attempts to upload your actions to the database (and changes some visuals).
     * The timer stops when the game is finished.
     *
     * @return true if ship positions are valid and were uploaded to the database
     */
    private boolean readyGame(){
        if (!gameReady) {
            ArrayList<Ship> overlappingShips = board1.uploadShipCoordinates();
            if (overlappingShips == null) {
                //If no ships are overlapping (the ships have been uploaded)
                gameReady = true;
                board1.setShipsMouseTransparent(true);
                gameReadyButton.setText("Waiting for opponent");
                gameReadyButton.setDisableVisualFocus(true);
                Statics.getGame().setShipsMovable(false);
                mouseFollower.setVisible(true);
                AnimationTimer animationTimer = new AnimationTimer() {
                    @Override
                    public void handle(long now) {
                        if (boardsReady == 1) {
                            updateBoards();
                            updateText();
                        }
                        Statics.getGame().doCachedActions();
                        updateBoardShadows();
                        if (Statics.getGame().isGameOver()) {
                            endGame();
                            this.stop();
                        }
                    }
                };
                animationTimer.start();
                return true;
            } else {
                //FOR SHAKING THE WHOLE SCENE
//                Shaker shaker = new Shaker(gameMainPane);
//                shaker.shake();

                for (Ship ship : overlappingShips) {
                    Scaler scaler = new Scaler(ship);
                    scaler.play();
                }
            }
        }
        return false;
    }

    /**
     * Moves the mouseFollower if to the parameter's x and y if it is outside of the boards.
     * If the cursor is inside a board, the mouseFollower will be snapped to the closest tile in the grid.
     *
     * @param x the new x-position of the mouseFollower
     * @param y the new y-position of the mouseFollower
     */
    private void moveMouseFollower(double x, double y) {
        int boardNumber = getMouseBoardNumber();
        if (boardNumber == 1) {
            mouseFollower.setTilePos(board1.getTranslateX(), board1.getTranslateY(), board1.getMousePosX(), board1.getMousePosY());
        } else if (boardNumber == 2) {
            mouseFollower.setTilePos(board2.getTranslateX(), board2.getTranslateY(), board2.getMousePosX(), board2.getMousePosY());
        } else {
            mouseFollower.setPos(x, y);
        }
    }

    /**
     * Called by thread to signal that both boards are uploaded
     */
    public void boardsReady() {
        boardsReady = 1;
    }

    /**
     *Checks that the game has both a host and a joining player who have uploaded their boards to the database,
     *then downloads the opponents board and starts the game
     *
     * @return true if both boards are ready and the game was started
     */
    private boolean updateBoards() {
        Game game = Statics.getGame();
        if (game.getJoinUser() != null && game.getHostUser() != null) {
            game.setBoardsReady(true);
            int opponentid;
            if (Statics.getLocalUser().equals(game.getHostUser()))
                opponentid = game.getJoinUser().getUserId();
            else opponentid = game.getHostUser().getUserId();
//            System.out.println("JOIN: " + game.getJoinUser().getUserId());
//            System.out.println("HOST: " + game.getHostUser().getUserId());
            board2.loadShipsFromDatabase(game.getGameId(), opponentid);
            gameReadyButton.setVisible(false);
            boardsReady = 2;
            return true;
        }
        return false;
    }

    /**
     * Updates the shadows that indicate who's turn it is
     */
    private void updateBoardShadows() {
        if (Statics.getGame().isMyTurn()) {
            board1Shadow.setVisible(true);
            board2Shadow.setVisible(false);
        } else {
            board1Shadow.setVisible(false);
            board2Shadow.setVisible(true);
        }
    }

    /**
     * Updates the user's, opponent's and game's name it the game window
     */
    private void updateText() {
        Game game = Statics.getGame();
        BattleshipUser host = game.getHostUser();
        BattleshipUser join = game.getJoinUser();
        gameGameNameText.setText(game.getGameName());
        if (game.isHosting()) {
            if (host != null) gameUserNameText.setText(host.getUsername());
            if (join != null) gameOpponentNameText.setText(join.getUsername());
        } else {
            if (join != null) gameUserNameText.setText(join.getUsername());
            if (host != null) gameOpponentNameText.setText(host.getUsername());

        }
    }

    /**
     * Colors the mousefollower darker
     */
    private void colorMouseFollower() {
        colorMouseFollower(false);
    }

    /**
     *
     * @param removeColor
     */
    private void colorMouseFollower(boolean removeColor) {
        if (onSameTiles() == -1 || removeColor) {
            mouseFollower.pressed(false);
        } else {
            mouseFollower.pressed(true);
        }
    }

    /**
     * Ends the game, tells a DatabaseConnector to change the users' scores in the database and then switches to a result scene.
     */
    private void endGame() {
        DatabaseConnector connector = new DatabaseConnector();
        if (Statics.getGame().getGameResult() == 1) {
            connector.updateUserScore(Statics.getLocalUser().getUserId(), 1);
        } else if (Statics.getGame().getGameResult() == 0) {
            connector.updateUserScore(Statics.getLocalUser().getUserId(), 0);
        }
        switchView("GameResultMenu", true);
    }

    /**
     * Method to get the main AnchorPane of this controller's fxml file
     *
     * @return the main AnchorPane of this controller's fxml file
     */
    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) gameMainPane.getParent();
    }
}
