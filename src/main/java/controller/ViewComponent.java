/**
 * controller.ViewComponent.java
 *
 * Abstract class inherited by view-components that want to be able to switch the parent AnchorPane's view
 *
 * @Author Thorkildsen Torje
 */

package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Paths;

public abstract class ViewComponent {

    /**
     * Method for switching the view of the parent AnchorPane of the class who inherits this method
     *
     * @param view the name of the fxml file you want to switch to, without path or ".fxml"
     * @return boolean, true if the view switch went well and false if not
     */
    public boolean switchView(String view){
        try {
            URL url = Paths.get("./src/main/java/view/" + view + ".fxml").toUri().toURL();
            AnchorPane formPane = FXMLLoader.load(url);
            getParentAnchorPane().getChildren().setAll(formPane);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean startGame(){
        //THIS IS FOR A NEW WINDOW
//        try {
//            getParentAnchorPane().getScene().getWindow().hide();
//            FXMLLoader loader = new FXMLLoader();
//            URL url = Paths.get("./src/main/java/view/Game.fxml").toUri().toURL();
//            loader.setLocation(url);
//            loader.load();
//            Parent root = loader.getRoot();
//            Stage stage = new Stage();
//            stage.setScene(new Scene(root));
//            stage.show();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;

        try {
            URL url = Paths.get("./src/main/java/view/Game.fxml").toUri().toURL();
            AnchorPane formPane = FXMLLoader.load(url);
            AnchorPane parent = (AnchorPane) getParentAnchorPane().getScene().getRoot();
            parent.getChildren().setAll(formPane);
            Window window = parent.getScene().getWindow();
            window.setWidth(formPane.getPrefWidth());
            double heightDiff = window.getHeight() - parent.getScene().getHeight();
            window.setHeight(formPane.getPrefHeight()+heightDiff);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * Gets the AnchorPane we need to switch between views in the SwitchView method
     * @return the parent AnchorPane the components (TextFields, Buttons etc) are part of
     */
    protected abstract AnchorPane getParentAnchorPane();
}
