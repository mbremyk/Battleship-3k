/**
 * SignupController.java
 *
 * <p>
 * Controller for the signup menu
 * </p>
 *
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
            termsOfServiceClicked();
        });

        signupSignupButton.setOnAction(event -> {
            signupButtonClicked();
        });

        signupUsernameField.setOnAction(event -> {
            signupButtonClicked();
        });
        signupEmailField.setOnAction(event -> {
            signupButtonClicked();
        });
        signupPasswordField.setOnAction(event -> {
            signupButtonClicked();
        });
        signupConfirmPasswordField.setOnAction(event -> {
            signupButtonClicked();
        });
    }

    /**
     * Opens a link to our Terms of Service in an internet browser
     */
    public void termsOfServiceClicked() {
        try {
            Desktop.getDesktop().browse(new URI("https://l.facebook.com/l.php?u=https%3A%2F%2Fdocs.google.com%2Fdocument%2Fd%2F1Baax3OXZ--mQsO4eGXhF-Lex8sRqVlXF1Q1liVNr_ms%2Fedit%3F" +
                    "usp%3Dsharing%26fbclid%3DIwAR2_aZAr_IYbRYCLtvy7knyu7IDggeo0XhMhNldO1fdeemj0uoNvoxJW0l4&h=AT1PMrDvdy8uwx_tmNeYjqnlAT15EwTooRahaMTfUT36zgUGDDl3hbLDQ71MBiI_LzPa0Z" +
                    "IEtu8VolT3PmJFhPiuGd6UnRUIZibBYYxzsMBpfSSbcZcFK_BTyz63oTst7Fj3JkAtKAwiN_E8-eIarg"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the input fields are valid and signs the user up by adding their user to the database.
     * The user is logged in afterwards.
     * If input is invalid the input fields will do a small shaking effect
     */
    public void signupButtonClicked() {
        if (checkValidUser()) {
            //Sets userid to -1 because the user hasn't been created yet so no userid exists in the database
            BattleshipUser user = new BattleshipUser(-1, signupUsernameField.getText(), signupPasswordField.getText(), signupEmailField.getText());
            boolean signupStatus = Login.registerUser(user);
            if (signupStatus) {
                signupHelpText.setText("New user was created");
//                    switchView("LoginMenu");
                Statics.setLocalUser(user);
                Scene scene = signupCancelButton.getScene();
                Node node = scene.lookup("#mainMenuLoggedInText");
                ((Text) node).setText("Logged in as " + signupUsernameField.getText());
                switchView("MainMenu");

            } else {
                signupHelpText.setText("Something went wrong");
            }
        } else {
            for (Node node : getParentAnchorPane().getChildren()) {
                if (node != signupUsernameField && node != signupEmailField && node != signupPasswordField && node != signupConfirmPasswordField && node != signupAgreeCheckbox && node != signupTOSLink) {
                    if (node instanceof Text) {
                        String text = ((Text) node).getText();
                        if (!text.equals("Username") && !text.equals("Email") && !text.equals("Password") && !text.equals("Confirm password")) {
                            continue;
                        }
                    } else {
                        continue;
                    }
                }
                Shaker shaker = new Shaker(node);
                shaker.shake();
            }
        }
    }


    /**
     * Checks if the text fields have valid input for a new user and that the username is not already in use
     *
     * @return true if a new user can be created with the text field inputs
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

    /**
     * Method to get the main AnchorPane of this controller's fxml file
     *
     * @return the main AnchorPane of this controller's fxml file
     */
    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane) signupCancelButton.getParent();
    }
}
