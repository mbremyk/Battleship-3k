package controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
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

    }
}
