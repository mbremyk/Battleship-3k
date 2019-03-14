/**
 *
 *
 * @Author Thorkildsen Torje
 */

package main.java.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class HostGameController extends ViewComponent{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton hostGameCreateGameButton;

    @FXML
    private TextField hostGameNameField;

    @FXML
    private PasswordField hostGamePasswordField;

    @FXML
    private JFXButton hostGameCancelButton;

    @FXML
    void initialize() {
        assert hostGameCreateGameButton != null : "fx:id=\"hostGameCreateGameButton\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert hostGameNameField != null : "fx:id=\"hostGameNameField\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert hostGamePasswordField != null : "fx:id=\"hostGamePasswordField\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert hostGameCancelButton != null : "fx:id=\"hostGameCancelButton\" was not injected: check your FXML file 'LoginMenu.fxml'.";

        hostGameCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });
    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) hostGameCancelButton.getParent();
    }
}
