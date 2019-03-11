/**
 * controller.ViewComponent.java
 *
 * Abstract class inherited by view-components that want to be able to switch the parent AnchorPane's view
 *
 * @Author Thorkildsen Torje
 */
package controller;

import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;

public abstract class ViewComponent {

    /**
     * Method for switching the view of the parent AnchorPane of the class who inherits this method
     *
     * @param view the name of the fxml file you want to switch to, without path or ".fxml"
     * @return boolean, true if the view switch went well and false if not
     */
    public boolean switchView(String view){
        try {
            AnchorPane formPane = formPane = FXMLLoader.load(getClass().getResource("/view/"+view+".fxml"));
            getParentAnchorPane().getChildren().setAll(formPane);
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
