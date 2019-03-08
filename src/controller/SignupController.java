package controller;

import com.jfoenix.controls.JFXButton;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class SignupController extends ViewComponent {

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
    private JFXButton SignupCancelButton;

    @FXML
    void initialize() {
        assert SignupUsernameField != null : "fx:id=\"SignupUsernameField\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert SignupEmailField != null : "fx:id=\"SignupEmailField\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert SignupConfirmPasswordField != null : "fx:id=\"SignupConfirmPasswordField\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert SignupSignupButton != null : "fx:id=\"SignupLoginButton\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert SignupPasswordField != null : "fx:id=\"SignupPasswordField\" was not injected: check your FXML file 'SignupMenu.fxml'.";
        assert SignupCancelButton != null : "fx:id=\"SignupCancelButton\" was not injected: check your FXML file 'SignupMenu.fxml'.";

        SignupCancelButton.setOnAction(event -> {
            switchView("MainMenu");
        });

        SignupSignupButton.setOnAction(event -> {
            //TODO, this is where we add the new user to the database, if it is valid
            System.out.println("TODO, see view/SignupController.java");
        });

    }

    @Override
    protected AnchorPane getParentAnchorPane() {
        return (AnchorPane)SignupCancelButton.getParent();
    }
}
