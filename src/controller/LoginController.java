package controller;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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
    private PasswordField LoginPasswordField;

    @FXML
    private JFXButton LoginCancelButton;

    @FXML
    private JFXButton LoginLoginButton;

    @FXML
    private Hyperlink LoginForgotPasswordLink;

    @FXML
    private TextField LoginUsernameField;

    @FXML
    void initialize() {
        assert LoginPasswordField != null : "fx:id=\"LoginPasswordField\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert LoginCancelButton != null : "fx:id=\"LoginCancelButton\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert LoginLoginButton != null : "fx:id=\"LoginLoginButton\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert LoginForgotPasswordLink != null : "fx:id=\"LoginForgotPasswordLink\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert LoginUsernameField != null : "fx:id=\"LoginUsernameField\" was not injected: check your FXML file 'LoginMenu.fxml'.";


        LoginCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });

        LoginLoginButton.setOnAction(event -> {
            //TODO, this is where we check if the username and password are valid
            System.out.println("TODO, see view/LoginController.java, but:\n" +
                    "Username = " +LoginUsernameField.getText()+
                    "\nPassword = " +LoginPasswordField.getText());
        });

    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane)LoginCancelButton.getParent();
    }
}
