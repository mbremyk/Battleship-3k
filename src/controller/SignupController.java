/**
 * @Author Thorkildsen Torje
 */
package controller;

import com.jfoenix.controls.JFXButton;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXCheckBox;
import database.Login;
import javafx.fxml.FXML;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import model.BattleshipUser;

public class SignupController extends ViewComponent {
    private final static int USERNAME_MAX_LENGTH = 20;
    private final static int PASSWORD_MAX_LENGTH = 40;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField SignupUsernameField;

    @FXML
    private TextField SignupEmailField;

    @FXML
    private PasswordField SignupConfirmPasswordField;

    @FXML
    private JFXButton SignupSignupButton;

    @FXML
    private PasswordField SignupPasswordField;

    @FXML
    private JFXCheckBox SignupAgreeCheckbox;

    @FXML
    private Text SignupHelpText;

    @FXML
    private JFXButton SignupCancelButton;

    @FXML
    private Hyperlink SignupTOSLink;

    @FXML
    void initialize() {
        assert SignupUsernameField != null : "fx:id=\"SignupUsernameField\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert SignupEmailField != null : "fx:id=\"SignupEmailField\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert SignupConfirmPasswordField != null : "fx:id=\"SignupConfirmPasswordField\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert SignupSignupButton != null : "fx:id=\"SignupSignupButton\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert SignupPasswordField != null : "fx:id=\"SignupPasswordField\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert SignupAgreeCheckbox != null : "fx:id=\"SignupAgreeCheckbox\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert SignupHelpText != null : "fx:id=\"SignupHelpText\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert SignupCancelButton != null : "fx:id=\"SignupCancelButton\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert SignupTOSLink != null : "fx:id=\"SignupTOSLink\" was not injected: check your FXML file 'SignupMenu.fxml'.";

        SignupCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });

        SignupTOSLink.setOnAction(event -> {
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

        SignupSignupButton.setOnAction(event -> {
            if (checkValidUser()) {
                BattleshipUser user = new BattleshipUser(SignupUsernameField.getText(), SignupPasswordField.getText(), SignupEmailField.getText());
                boolean signupStatus = Login.registerUser(user);
                if (signupStatus) {
                    SignupHelpText.setText("New user was created");
                } else {
                    SignupHelpText.setText("Something went wrong");
                }
            }

        });


    }


    /**
     * @return
     */
    private boolean checkValidUser() {
        if (SignupUsernameField.getText().length() > USERNAME_MAX_LENGTH) {
            SignupHelpText.setText("Username is too long, max is " + USERNAME_MAX_LENGTH + " characters");
            return false;
        } else if (SignupUsernameField.getText().equals("")) {
            SignupHelpText.setText("Username can't be empty");
            return false;
        } else if (Login.usernameExists(SignupUsernameField.getText())) {
            SignupHelpText.setText("That username is already in use");
            return false;
        } else if (Login.emailExists(SignupEmailField.getText())) {
            SignupHelpText.setText("That email is already in use");
            return false;
        } else if (SignupEmailField.getText().indexOf('@') <= 0 || SignupEmailField.getText().indexOf('.') <= 2) {
            //TODO improve this if-sentence
            SignupHelpText.setText("Not a valid email address");
            return false;
        } else if (SignupPasswordField.getText().length() > PASSWORD_MAX_LENGTH) {
            SignupHelpText.setText("Password is too long, max is " + PASSWORD_MAX_LENGTH + " characters");
            return false;
        } else if (SignupPasswordField.getText().equals("")) {
            SignupHelpText.setText("Password can't be empty");
            return false;
        } else if (!SignupPasswordField.getText().equals(SignupConfirmPasswordField.getText())) {
            SignupHelpText.setText("Passwords are not matching");
            return false;
        } else if (!SignupAgreeCheckbox.isSelected()) {
            SignupHelpText.setText("You have to agree to our Terms of Service");
            return false;
        }
        return true;
    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) SignupCancelButton.getParent();
    }
}
