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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

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
            //TODO, this is where we check if the username and password are valid
            System.out.println("TODO, see view/LoginController.java, but:\n" +
                    "Username = " +loginUsernameField.getText()+
                    "\nPassword = " +loginPasswordField.getText());
        });

    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane)loginCancelButton.getParent();
    }
}
