/**
 * LoginController.java
 *
 * <p>
 * Controller for the login menu
 * </p>
 *
 * @author Thorkildsen Torje
 */
package controller;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.ResourceBundle;

import database.Login;
import effects.Shaker;
import game.Statics;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.BattleshipUser;

public class LoginController extends ViewComponent {

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
    private TextField loginUsernameField;


    @FXML
    void initialize() {
        assert loginPasswordField != null : "fx:id=\"loginPasswordField\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert loginCancelButton != null : "fx:id=\"loginCancelButton\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert loginLoginButton != null : "fx:id=\"loginLoginButton\" was not injected: check your FXML file 'LoginMenu.fxml'.";
        assert loginUsernameField != null : "fx:id=\"loginUsernameField\" was not injected: check your FXML file 'LoginMenu.fxml'.";

        loginUsernameField.setOnAction(event -> {
            loginButtonPressed();
        });

        loginPasswordField.setOnAction(event -> {
            loginButtonPressed();
        });

        loginCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });

        loginLoginButton.setOnAction(event -> {
            loginButtonPressed();
        });
    }

    /**
     * Checks if the inputted username exists and if the password is correct, then logs the user in
     */
    public void loginButtonPressed() {
        BattleshipUser user = Login.login(loginUsernameField.getText(), loginPasswordField.getText());
        if (user != null) {
            Statics.setLocalUser(user);
            Scene scene = loginCancelButton.getScene();
            Node node = scene.lookup("#mainMenuLoggedInText");
            ((Text) node).setText("Logged in as " + loginUsernameField.getText());
            switchView("MainMenu");
        } else {
            for (Node node : getParentAnchorPane().getChildren()) {
                if (node != loginUsernameField && node != loginPasswordField) {
                    if (node instanceof Text) {
                        String text = ((Text) node).getText();
                        if (!text.equals("Username") && !text.equals("Password")) {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
                Shaker shaker = new Shaker(node);
                shaker.play();
            }
        }
    }

    /**
     * Method to get the main AnchorPane of this controller's fxml file
     *
     * @return the main AnchorPane of this controller's fxml file
     */
    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) loginCancelButton.getParent();
    }
}
