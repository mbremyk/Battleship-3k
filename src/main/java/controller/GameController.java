package controller;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.ResourceBundle;

import game.Board;
import game.MouseFollower;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
    private JFXButton gameTurnButton;

    @FXML
    private Text gameUserNameText;

    @FXML
    private AnchorPane gameMainPane;

    @FXML
    private ImageView gameOptionsImage;


    @FXML
    void initialize() {
        assert gameToolsPane != null : "fx:id=\"gameToolsPane\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameOpponentNameText != null : "fx:id=\"gameOpponentNameText\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameAttacksPane != null : "fx:id=\"gameAttacksPane\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameGameNameText != null : "fx:id=\"gameGameNameText\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameTurnButton != null : "fx:id=\"gameTurnButton\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameUserNameText != null : "fx:id=\"gameUserNameText\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameMainPane != null : "fx:id=\"gameMainPane\" was not injected: check your FXML file 'Game.fxml'.";
        assert gameOptionsImage != null : "fx:id=\"gameOptionsImage\" was not injected: check your FXML file 'Game.fxml'.";

        Board board1 = new Board(1);
        board1.setTranslateX(250);
        board1.setTranslateY(200);
        Board board2 = new Board(2);
        board2.setTranslateX(650);
        board2.setTranslateY(200);
        MouseFollower mouseFollower = new MouseFollower();
        gameMainPane.getChildren().addAll(board1, board2, mouseFollower);

        gameMainPane.setOnMouseMoved(event -> {
            if (board1.getMousePosX() != -1 && board1.getMousePosY() != -1) {
                mouseFollower.setTilePos(board1.getTranslateX(), board1.getTranslateY(),board1.getMousePosX(),board1.getMousePosY());
            } else if (board2.getMousePosX() != -1 && board2.getMousePosY() != -1) {
                mouseFollower.setTilePos(board2.getTranslateX() , board2.getTranslateY(),board2.getMousePosX(), board2.getMousePosY());
            } else {
                mouseFollower.setPos(event.getX(), event.getY());
            }
        });
    }
}
