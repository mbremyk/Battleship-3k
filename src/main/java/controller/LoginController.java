/**
 *
 *
 * @Author Thorkildsen Torje
 */
package controller;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.ResourceBundle;

import database.Login;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class LoginController extends ViewComponent{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private PasswordField loginPasswordField;

    @FXML
    private JFXButton loginCancelButton;

    @FXML
    private JFXButton loginLoginButton;

    @FXML
    private Hyperlink loginForgotPasswordLink;

    @FXML
    private TextField loginUsernameField;

    /**
     *
     */

    @FXML
    void initialize() {
        assert loginPasswordField != null : "fx:id=\"loginPasswordField\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert loginCancelButton != null : "fx:id=\"loginCancelButton\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert loginLoginButton != null : "fx:id=\"loginLoginButton\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert loginForgotPasswordLink != null : "fx:id=\"loginForgotPasswordLink\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert loginUsernameField != null : "fx:id=\"loginUsernameField\" was not injected: check your FXML file 'LoginMenu.fxml'.";


        loginCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });
    
        loginLoginButton.setOnAction(event -> {
            if (Login.login(loginUsernameField.getText(), loginPasswordField.getText()) != null){
                Scene scene = loginCancelButton.getScene();
                Node node = scene.lookup("#mainMenuLoggedInText");
                ((Text)node).setText("Logged in as " + loginUsernameField.getText());
                switchView("MainMenu");
            }
        });
    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane)loginCancelButton.getParent();
    }
}
