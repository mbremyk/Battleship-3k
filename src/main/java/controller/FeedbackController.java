/**
 * FeedbackController.java
 *
 * <p>
 * Controller class for FeedbackMenu.fxml
 * Allows the user to write feedback and upload it to the database
 * </p>
 *
 * @Author Thorkildsen Torje
 */

package controller;

import com.jfoenix.controls.JFXButton;

import java.net.URL;
import java.util.ResourceBundle;

import database.DatabaseConnector;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class FeedbackController extends ViewComponent {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private JFXButton feedbackSubmitButton;

    @FXML
    private JFXButton feedbackCancelButton;

    @FXML
    private TextArea titleTextArea;

    @FXML
    private TextArea feedbackTextArea;

    @FXML
    private Text charText;

    @FXML
    void initialize() {
        assert feedbackSubmitButton != null : "fx:id=\"feedbackSubmitButton\" was not injected: check your FXML file 'FeedbackMenu.fxml'.";
        assert feedbackCancelButton != null : "fx:id=\"feedbackCancelButton\" was not injected: check your FXML file 'FeedbackMenu.fxml'.";
        assert titleTextArea != null : "fx:id=\"titleTextArea\" was not injected: check your FXML file 'FeedbackMenu.fxml'.";
        assert feedbackTextArea != null : "fx:id=\"feedbackTextArea\" was not injected: check your FXML file 'FeedbackMenu.fxml'.";
        assert charText != null : "fx:id=\"charText\" was not injected: check your FXML file 'FeedbackMenu.fxml'.";


        int MAX_CHARS_FEEDBACK = 255;
        int MAX_CHARS_TITLE = 30;

        feedbackCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });
        feedbackTextArea.textProperty().addListener(((observable, oldValue, newValue) -> textFieldChange(newValue)));

        titleTextArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= MAX_CHARS_TITLE ? change : null));

        feedbackTextArea.setTextFormatter(new TextFormatter<String>(change ->
                change.getControlNewText().length() <= MAX_CHARS_FEEDBACK ? change : null));

        feedbackTextArea.setWrapText(true);

        feedbackSubmitButton.setOnAction(event -> {
            uploadFeedback();
        });

    }

    /**
     * Uploads the written feedback via a DatabaseConnector
     */
    private void uploadFeedback() {
        DatabaseConnector connector = new DatabaseConnector();
        if (connector.uploadFeedback(titleTextArea.getText(), feedbackTextArea.getText())) {
            charText.setText("Thank you for your feedback!");
        }
    }

    /**
     * Signals that the text field has changed,
     * and updates the text showing maximum characters in the text field
     *
     * @param newValue the new string in the message box
     */
    private void textFieldChange(String newValue) {
        charText.setText("" + newValue.length() + "/255 characters");
    }

    /**
     * Method to get the main AnchorPane of this controller's fxml file
     *
     * @return the main AnchorPane of this controller's fxml file
     */
    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) feedbackCancelButton.getParent();
    }
}
