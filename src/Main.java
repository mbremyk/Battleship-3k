import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class Main extends Application {

	/**
	 * Overridden method from Application in JavaFX
	 * Called in the JavaFX Application Thread when the application is ready to run
	 * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Font.loadFont(getClass().getResourceAsStream("/PixelTorje.ttf"), 20);
			Font.loadFont(getClass().getResourceAsStream("/PixelTorje.ttf"), 36);
			Font.loadFont(getClass().getResourceAsStream("/PixelTorje.ttf"), 96);
 			Parent root = FXMLLoader.load(getClass().getResource("view/MenuTemplate.fxml"));
			
 			final Scene scene = new Scene(root);
 			//scene.getStylesheets().add(getClass().getResource("custom-font-style.css").toExternalForm());
			primaryStage.setScene(scene);
			primaryStage.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Main method
	 * @param args
	 */
	public static void main(String[] args){
		launch(args);
	}
}
