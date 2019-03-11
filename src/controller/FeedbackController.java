package controller;

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
    private JFXButton FeedbackSubmitButton;

    @FXML
    private JFXButton FeedbackCancelButton;

    @FXML
    void initialize() {
        assert FeedbackSubmitButton != null : "fx:id=\"FeedbackSubmitButton\" was not injected: check your FXML file 'FeedbackMenu.fxml'.";
        assert FeedbackCancelButton != null : "fx:id=\"FeedbackCancelButton\" was not injected: check your FXML file 'FeedbackMenu.fxml'.";

        FeedbackCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });
    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) FeedbackCancelButton.getParent();
    }
}
