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
import game.*;
import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import model.BattleshipUser;

public class GameController extends ViewComponent{

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
    private boolean gameReadyButtonPressed = false;

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
        addUIComponents();
        updateText();
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
//                System.out.println("Placing boat on " + board1.getMousePosX() + "," + board1.getMousePosY());
            } else if (boardNumber == 2 && Statics.getGame().isMyTurn()) {
                int attackX = board2.getMousePosX();
                int attackY = board2.getMousePosY();
                board2.attack(attackX, attackY);
            }
            moveMouseFollower(event.getX(), event.getY());
            colorMouseFollower(true);
        });

        gameReadyButton.setOnAction(event -> {
            if (!gameReadyButtonPressed) {
                ArrayList<Ship> overlappingShips = board1.uploadShipCoordinates();
                if (overlappingShips == null) {
                    //If no ships are overlapping (the ships have been uploaded)
                    gameReadyButtonPressed = true;
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
                            if (Statics.getGame().isGameOver()){
                                endGame();
                                this.stop();
                            }
                        }
                    };
                    animationTimer.start();
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

    //returns board number if cursor is on same tiles as when pressed
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
        Game game = Statics.getGame();
        if (game.getJoinUser() != null && game.getHostUser() != null){
            game.setBoardsReady(true);
            int opponentid;
            if (Statics.getLocalUser().equals(game.getHostUser()))
                opponentid = game.getJoinUser().getUserId();
            else opponentid = game.getHostUser().getUserId();
            System.out.println("JOIN: " + game.getJoinUser().getUserId());
            System.out.println("HOST: " + game.getHostUser().getUserId());
            board2.loadShipsFromDatabase(game.getGameId(), opponentid);
            gameReadyButton.setVisible(false);
            boardsReady = 2;
        }
    }

    private void updateText(){
        Game game = Statics.getGame();
        BattleshipUser host = game.getHostUser();
        BattleshipUser join = game.getJoinUser();
        gameGameNameText.setText(game.getGameName());
        if(game.isHosting()) {
            if(host != null)gameUserNameText.setText(host.getUsername());
            if(join != null)gameOpponentNameText.setText(join.getUsername());
        }else{
            if(join != null)gameUserNameText.setText(join.getUsername());
            if(host != null)gameOpponentNameText.setText(host.getUsername());

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
    private void endGame(){
        if(Statics.getGame().getGameResult() == 1){
            DatabaseConnector connector = new DatabaseConnector();
            connector.updateUserScore(Statics.getLocalUser().getUsername(),1);
            switchView("GameResultMenu");
        }
        else if(Statics.getGame().getGameResult() == 0){
            DatabaseConnector connector = new DatabaseConnector();
            connector.updateUserScore(Statics.getLocalUser().getUsername(),0);
            switchView("GameResultMenu");
        }
    }
    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) gameToolsPane.getParent();
    }
}
