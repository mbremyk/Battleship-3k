/**
 * @Author Thorkildsen Torje
 */
package main.java.controller;

import com.jfoenix.controls.JFXButton;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;
import main.java.database.Login;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import main.java.model.BattleshipUser;

public class SignupController extends ViewComponent {
    private final static int USERNAME_MAX_LENGTH = 20;
    private final static int PASSWORD_MAX_LENGTH = 40;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField signupUsernameField;

    @FXML
    private TextField signupEmailField;

    @FXML
    private PasswordField signupConfirmPasswordField;

    @FXML
    private JFXButton signupSignupButton;

    @FXML
    private PasswordField signupPasswordField;

    @FXML
    private JFXCheckBox signupAgreeCheckbox;

    @FXML
    private Text signupHelpText;

    @FXML
    private JFXButton signupCancelButton;

    @FXML
    private Hyperlink signupTOSLink;

    @FXML
    void initialize() {
        assert signupUsernameField != null : "fx:id=\"signupUsernameField\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert signupEmailField != null : "fx:id=\"signupEmailField\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert signupConfirmPasswordField != null : "fx:id=\"signupConfirmPasswordField\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert signupSignupButton != null : "fx:id=\"signupSignupButton\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert signupPasswordField != null : "fx:id=\"signupPasswordField\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert signupAgreeCheckbox != null : "fx:id=\"signupAgreeCheckbox\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert signupHelpText != null : "fx:id=\"signupHelpText\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert signupCancelButton != null : "fx:id=\"signupCancelButton\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert signupTOSLink != null : "fx:id=\"signupTOSLink\" was not injected: check your FXML file 'SignupMenu.fxml'.";

        signupCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });

        signupTOSLink.setOnAction(event -> {
            try {
                Desktop.getDesktop().browse(new URI("https://l.facebook.com/l.php?u=https%3A%2F%2Fdocs.google.com%2Fdocument%2Fd%2F1Baax3OXZ--mQsO4eGXhF-Lex8sRqVlXF1Q1liVNr_ms%2Fedit%3F" +
                        "usp%3Dsharing%26fbclid%3DIwAR2_aZAr_IYbRYCLtvy7knyu7IDggeo0XhMhNldO1fdeemj0uoNvoxJW0l4&h=AT1PMrDvdy8uwx_tmNeYjqnlAT15EwTooRahaMTfUT36zgUGDDl3hbLDQ71MBiI_LzPa0Z" +
                        "IEtu8VolT3PmJFhPiuGd6UnRUIZibBYYxzsMBpfSSbcZcFK_BTyz63oTst7Fj3JkAtKAwiN_E8-eIarg"));
            } catch (IOException e) {
                e.printStackTrace();
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        });

        signupSignupButton.setOnAction(event -> {
            if (checkValidUser()) {
                BattleshipUser user = new BattleshipUser(signupUsernameField.getText(), signupPasswordField.getText(), signupEmailField.getText());
                boolean signupStatus = Login.registerUser(user);
                if (signupStatus) {
                    signupHelpText.setText("New user was created");
                } else {
                    signupHelpText.setText("Something went wrong");
                }
            }

        });


    }


    /**
     * @return
     */
    private boolean checkValidUser() {
        if (signupUsernameField.getText().length() > USERNAME_MAX_LENGTH) {
            signupHelpText.setText("Username is too long, max is " + USERNAME_MAX_LENGTH + " characters");
            return false;
        } else if (signupUsernameField.getText().equals("")) {
            signupHelpText.setText("Username can't be empty");
            return false;
        } else if (Login.usernameExists(signupUsernameField.getText())) {
            signupHelpText.setText("That username is already in use");
            return false;
        } else if (Login.emailExists(signupEmailField.getText())) {
            signupHelpText.setText("That email is already in use");
            return false;
        } else if (signupEmailField.getText().indexOf('@') <= 0 || signupEmailField.getText().indexOf('.') <= 2) {
            //TODO improve this if-sentence
            signupHelpText.setText("Not a valid email address");
            return false;
        } else if (signupPasswordField.getText().length() > PASSWORD_MAX_LENGTH) {
            signupHelpText.setText("Password is too long, max is " + PASSWORD_MAX_LENGTH + " characters");
            return false;
        } else if (signupPasswordField.getText().equals("")) {
            signupHelpText.setText("Password can't be empty");
            return false;
        } else if (!signupPasswordField.getText().equals(signupConfirmPasswordField.getText())) {
            signupHelpText.setText("Passwords are not matching");
            return false;
        } else if (!signupAgreeCheckbox.isSelected()) {
            signupHelpText.setText("You have to agree to our Terms of Service");
            return false;
        }
        return true;
    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) signupCancelButton.getParent();
    }
}
