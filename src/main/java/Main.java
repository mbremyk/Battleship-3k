/**
 * Main.java
 *
 * @Author Brevik Magnus
 * @Author Bjerke Thomas
 * @Author Gulaker Kristian
 * @Author Thorkildsen Torje
 * @Author Granli Hans Kristian Olsen
 * @Author Grande Trym
 */

import game.Game;
import database.ConnectionPool;
import database.DatabaseConnector;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;

import static database.Constants.DB_URL;

public class Main extends Application {
	public static ConnectionPool connectionPool = null;
	/**
	 * Overridden method from Application in JavaFX
	 * Called in the JavaFX Application Thread when the application is ready to run
	 * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) {
		try {
			Font.loadFont(getClass().getResourceAsStream("PixelTorje.ttf"), 20);
			Font.loadFont(getClass().getResourceAsStream("PixelTorje.ttf"), 28);
			Font.loadFont(getClass().getResourceAsStream("PixelTorje.ttf"), 36);
			Font.loadFont(getClass().getResourceAsStream("PixelTorje.ttf"), 96);
			URL url = Paths.get("src/main/java/view/MenuTemplate.fxml").toUri().toURL();
 			Parent root = FXMLLoader.load(url);
			
 			final Scene scene = new Scene(root);
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
		try {
			connectionPool = ConnectionPool.create(DB_URL);
			DatabaseConnector databaseConnector = new DatabaseConnector(DB_URL);
			databaseConnector.setConnectionPool(connectionPool);
		}
		catch(SQLException e){
			e.printStackTrace();
		}
		launch(args);
	}

}
