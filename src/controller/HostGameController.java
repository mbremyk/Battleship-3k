/**
 *
 *
 * @Author Thorkildsen Torje
 */

package controller;

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
    private JFXButton HostGameCreateGameButton;

    @FXML
    private TextField HostGameNameField;

    @FXML
    private PasswordField HostGamePasswordField;

    @FXML
    private JFXButton HostGameCancelButton;

    @FXML
    void initialize() {
        assert HostGameCreateGameButton != null : "fx:id=\"HostGameCreateGameButton\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert HostGameNameField != null : "fx:id=\"HostGameNameField\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert HostGamePasswordField != null : "fx:id=\"HostGamePasswordField\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert HostGameCancelButton != null : "fx:id=\"HostGameCancelButton\" was not injected: check your FXML file 'LoginMenu.fxml'.";

        HostGameCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });
    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) HostGameCancelButton.getParent();
    }
}
