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

import com.sun.javafx.fxml.PropertyNotFoundException;
import game.Game;
import database.ConnectionPool;
import database.DatabaseConnector;
import game.Statics;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

import database.Constants;

import static database.Login.logout;

public class Main extends Application {
    public static ConnectionPool connectionPool = null;

    /**
     * Overridden method from Application in JavaFX
     * Called in the JavaFX Application Thread when the application is ready to run
     *
     * @param primaryStage
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            Font.loadFont(getClass().getResourceAsStream("PixelTorje.ttf"), 18);
            Font.loadFont(getClass().getResourceAsStream("PixelTorje.ttf"), 20);
            Font.loadFont(getClass().getResourceAsStream("PixelTorje.ttf"), 28);
            Font.loadFont(getClass().getResourceAsStream("PixelTorje.ttf"), 36);
            Font.loadFont(getClass().getResourceAsStream("PixelTorje.ttf"), 96);
            URL url = Paths.get("src/main/java/view/MenuTemplate.fxml").toUri().toURL();
            Parent root = FXMLLoader.load(url);

            final Scene scene = new Scene(root);
            primaryStage.setScene(scene);
            primaryStage.show();
            primaryStage.setResizable(false);

            primaryStage.setOnCloseRequest(e -> {
                closePage();
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Main method
     *
     * @param args standard args. ARRRGH
     */
    public static void main(String[] args) {
        try{
            Properties p = new Properties();

            InputStream in = new FileInputStream("src/main/java/dbConfig.properties");

            p.load(in);

            Constants.setDatabaseValues(p.getProperty("dbHost"), p.getProperty("dbPort"), p.getProperty("dbName"), p.getProperty("password"), p.getProperty("username"));


        } catch (FileNotFoundException e){
            System.out.println("Fil ikke funnet");
        } catch (IOException e){
            e.printStackTrace();
        } catch (PropertyNotFoundException e){

        }
        try {
            connectionPool = ConnectionPool.create(Constants.getDbUrl());
            DatabaseConnector databaseConnector = new DatabaseConnector(Constants.getDbUrl());

            databaseConnector.setConnectionPool(connectionPool);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        launch(args);
    }

    public static void closePage() {
        Game game = Statics.getGame();
        if (game != null && !game.isGameOver()) {
            game.setMyTurn(false);
            game.setLoser(Statics.getLocalUser());
            DatabaseConnector db = new DatabaseConnector();
            db.uploadResults();
        }
        Statics.setGame(game);
        logout();
        if (Statics.getPullThread() != null && Statics.getPullThread().isAlive()) {
            Statics.getPullThread().terminate();
        }
    }

}
