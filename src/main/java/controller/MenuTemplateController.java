/**
 * MenuTemplateController.java
 *
 * <p>
 * Controller for the template for the general menus. This is on top in the regular menu view
 * </p>
 *
 * @author Thorkildsen Torje
 */

package controller;

import com.jfoenix.controls.JFXButton;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;

import game.Statics;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import model.BattleshipUser;

public class MenuTemplateController extends ViewComponent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton mainMenuLoginButton;

    @FXML
    private JFXButton mainMenuSignupButton;

    @FXML
    private AnchorPane switchPane;

    @FXML
    private Text mainMenuLoggedInText;


    @FXML
    void initialize() {
        assert mainMenuLoginButton != null : "fx:id=\"mainMenuLoginButton\" was not injected: check your FXML file 'MenuTemplate.fxml'.";
        assert mainMenuSignupButton != null : "fx:id=\"mainMenuSignupButton\" was not injected: check your FXML file 'MenuTemplate.fxml'.";
        assert switchPane != null : "fx:id=\"switchPane\" was not injected: check your FXML file 'MenuTemplate.fxml'.";
        assert mainMenuLoggedInText != null : "fx:id=\"mainMenuLoggedInText\" was not injected: check your FXML file 'MenuTemplate.fxml'.";

        switchView("MainMenu");

        BattleshipUser user = Statics.getLocalUser();
        if (user != null) mainMenuLoggedInText.setText("Logged in as " + user.getUsername());

        mainMenuSignupButton.setOnAction(event -> {
            switchView("SignupMenu");
        });

        mainMenuLoginButton.setOnAction(event -> {
            switchView("LoginMenu");
        });
    }

    /**
     * Method to get the main AnchorPane of this controller's fxml file
     *
     * @return the main AnchorPane of this controller's fxml file
     */
    @Override
    protected AnchorPane getParentAnchorPane() {
        return switchPane;
    }

}
