/**
 *
 *
 * @Author Thorkildsen Torje
 */
package controller;

import com.jfoenix.controls.JFXButton;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

public class MenuTemplateController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton MainMenuLoginButton;

    @FXML
    private JFXButton MainMenuSignupButton;

    @FXML
    private AnchorPane switchPane;

    /**
     * Standard...
     */
    @FXML
    void initialize() {
        assert MainMenuLoginButton != null : "fx:id=\"MainMenuLoginButton\" was not injected: check your FXML file 'MenuTemplate.fxml'.";
        assert MainMenuSignupButton != null : "fx:id=\"MainMenuSignupButton\" was not injected: check your FXML file 'MenuTemplate.fxml'.";
        assert switchPane != null : "fx:id=\"switchPane\" was not injected: check your FXML file 'MenuTemplate.fxml'.";

        switchView("MainMenu");


        MainMenuSignupButton.setOnAction(event -> {
            switchView("SignupMenu");
        });

        MainMenuLoginButton.setOnAction(event -> {
            switchView("LoginMenu");
        });
    }

    /**
     * Handles...
     *
     * @param view
     * @return boolean representing whether the view-switch was successful or not
     */
    public boolean switchView(String view){
        try {
            AnchorPane formPane = formPane = FXMLLoader.load(getClass().getResource("/view/"+view+".fxml"));
            switchPane.getChildren().setAll(formPane);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

}
