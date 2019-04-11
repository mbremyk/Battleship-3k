package model;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AlertBox {
    public static void display(String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setWidth(350);
        window.setResizable(false);
        
        Label label = new Label(message);
        label.setFont(Font.font("PixelTorje",25));

        Button closeButton = new Button("OK!");
        closeButton.setMinWidth(50);
        closeButton.setAlignment(Pos.CENTER);
        closeButton.setFont(Font.font("PixelTorje",20));
        window.setOnCloseRequest(e -> e.consume());
        closeButton.setOnAction(e -> window.close());
        
        VBox layout = new VBox(15);
        layout.getChildren().addAll(label,closeButton);

        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        
    }
}
