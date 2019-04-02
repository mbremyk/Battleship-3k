/**
 * @Author Kristian Gulaker
 */

package controller;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import com.mysql.cj.result.Row;
import database.Constants;
import database.DatabaseConnector;
import game.Game;
import game.Statics;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.Window;
import model.BattleshipUser;

public class GameResultMenuController extends ViewComponent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ImageView gameResultMenuImage;

    @FXML
    private Text gameResultMenuText;

    @FXML
    private JFXButton returnButton;

    @FXML
    private JFXButton rematchButton;


    @FXML
    void initialize() {
        assert returnButton != null : "fx:id=\"returnButton\" was not injected: check your FXML file 'GameResultMenu.fxml'.";
        assert rematchButton != null : "fx:id=\"rematchButton\" was not injected: check your FXML file 'GameResultMenu.fxml'.";
        assert gameResultMenuText != null : "fx:id=\"gameResultMenuText\" was not injected: check your FXML file 'GameResultMenu.fxml'.";
        assert gameResultMenuImage != null : "fx:id=\"gameResultMenuImage\" was not injected: check your FXML file 'GameResultMenu.fxml'.";

        Image image;
        String endText;
        if (Statics.getGame().getGameResult() == 0) {
            endText = "YOU LOST!";
            image = new Image("/skull.png");
//            gameResultMenuImage.setFitHeight(200);
//            gameResultMenuImage.setFitWidth(200);
//            gameResultMenuImage.setX(300);
//            gameResultMenuImage.setY(200);
//            gameResultMenuText.setX(200);
//            gameResultMenuText.setY(150);
        } else {
            endText = "YOU WON!";
            image = new Image("/trophy.png");
        }
        gameResultMenuText.setText(endText);
        gameResultMenuImage.setImage(image);

        returnButton.setOnAction(e -> switchView("MenuTemplate", true));
    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) rematchButton.getParent();
    }

}
