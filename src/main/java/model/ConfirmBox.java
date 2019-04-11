package model;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class ConfirmBox{
    
   private static boolean answer;
   
   public static boolean display(String message){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.setMinWidth(250);
        window.setResizable(false);
        window.setOnCloseRequest(e -> e.consume());
        
        Label label = new Label(message);
        label.setFont(Font.font("PixelTorje",20));
        Button yesButton = new Button("YES");
        yesButton.setFont(Font.font("PixelTorje",15));
        yesButton.setOnAction(e -> {
            answer = true;
            window.close();
        });
        Button noButton = new Button("NO");
        noButton.setFont(Font.font("PixelTorje",15));
        noButton.setOnAction(e -> {
            answer = false;
            window.close();
        });
        HBox layoutButton = new HBox(20);
        layoutButton.getChildren().addAll(yesButton,noButton);
        VBox layout = new VBox(20);
        layout.getChildren().addAll(label,layoutButton);
        
        Scene scene = new Scene(layout);
        window.setScene(scene);
        window.showAndWait();
        
        return answer;
    } 
}
