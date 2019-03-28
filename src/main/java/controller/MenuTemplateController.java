/**
 *
 *
 * @Author Thorkildsen Torje
 */
package controller;

import com.jfoenix.controls.JFXButton;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

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
    private ImageView mainMenuOptionsImage;

    @FXML
    private Text mainMenuLoggedInText;



    /**
     * Standard...
     */
    @FXML
    void initialize() {
        assert mainMenuLoginButton != null : "fx:id=\"mainMenuLoginButton\" was not injected: check your FXML file 'MenuTemplate.fxml'.";
        assert mainMenuSignupButton != null : "fx:id=\"mainMenuSignupButton\" was not injected: check your FXML file 'MenuTemplate.fxml'.";
        assert switchPane != null : "fx:id=\"switchPane\" was not injected: check your FXML file 'MenuTemplate.fxml'.";
        assert mainMenuOptionsImage != null : "fx:id=\"mainMenuOptionsImage\" was not injected: check your FXML file 'MenuTemplate.fxml'.";
        assert mainMenuLoggedInText != null : "fx:id=\"mainMenuLoggedInText\" was not injected: check your FXML file 'MenuTemplate.fxml'.";

        switchView("MainMenu");


        mainMenuSignupButton.setOnAction(event -> {
            switchView("SignupMenu");
        });

        mainMenuLoginButton.setOnAction(event -> {
            switchView("LoginMenu");
        });
    }


//TODO REMOVE THIS?
//    /**
//     * Handles...
//     *
//     * @param view
//     * @return boolean representing whether the view-switch was successful or not
//     */
//    public boolean switchView(String view){
//        try {
//            URL url = Paths.get("src/main/java/view/" + view + ".fxml").toUri().toURL();
//            AnchorPane formPane = FXMLLoader.load(url);
//            switchPane.getChildren().setAll(formPane);
//            return true;
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return switchPane;
    }

}
