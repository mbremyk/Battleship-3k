

package main.java.controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;

public class FeedbackController extends ViewComponent{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton feedbackSubmitButton;

    @FXML
    private JFXButton feedbackCancelButton;

    @FXML
    void initialize() {
        assert feedbackSubmitButton != null : "fx:id=\"feedbackSubmitButton\" was not injected: check your FXML file 'FeedbackMenu.fxml'.";
        assert feedbackCancelButton != null : "fx:id=\"feedbackCancelButton\" was not injected: check your FXML file 'FeedbackMenu.fxml'.";

        feedbackCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });
    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) feedbackCancelButton.getParent();
    }
}
