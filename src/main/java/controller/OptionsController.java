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
import javafx.scene.layout.AnchorPane;

public class OptionsController extends ViewComponent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton OptionsCancelButton;

    /**
     *
     */
    @FXML
    void initialize() {
        assert OptionsCancelButton != null : "fx:id=\"LoginCancelButton\" was not injected: check your FXML file 'OptionsMenu.fxml'.";

        OptionsCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });

    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) OptionsCancelButton.getParent();
    }
}
