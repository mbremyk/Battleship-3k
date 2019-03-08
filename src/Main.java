import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.IOException;

public class Main extends Application {

	/**
	 * Overridden method from Application in JavaFX
	 * Called in the JavaFX Main Thread when the application is ready to run
	 * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("view/MainMenu.fxml"));

			primaryStage.setScene(new Scene(root));
			primaryStage.show();
		} catch (IOException e) {
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
