/**
 * Login.java
 *
 * <p>
 * Contains database constants such as table names and column names
 * </p>
 *
 * @author Thorkildsen Torje
 * @author Brevik Magnus
 * @author Hans Kristian Granli
 */

package database;

public class Constants {
    //Database connection configuration
    public static void setDatabaseValues(String dbHost, String dbPort, String dbUser, String dbPassword, String dbName){
        DB_HOST = dbHost;
        DB_PORT = dbPort;
        DB_USER = dbUser;
        DB_PASSWORD = dbPassword;
        DB_NAME = dbName;
        DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?user=" + DB_USER + "&password=" + DB_PASSWORD;
    }
    public static String DB_HOST = "";
    public static String DB_PORT = "";
    public static String DB_USER = "";
    public static String DB_PASSWORD = "";
    public static String DB_NAME = "";
    public static String DB_URL = "";
    /*
    public static String DB_HOST = "mysql.stud.idi.ntnu.no";
    public static String DB_PORT = "3306";
    public static String DB_USER = "thombje";
    public static String DB_PASSWORD = "TFWUfjmb";
    public static String DB_NAME = "thombje";
    public static String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?user=" + DB_USER + "&password=" + DB_PASSWORD;*/

    //Users table column names
    public static final String USERS_TABLE = "BattleshipUser";
    public static final String USERS_ID = "user_id";
    public static final String USERS_USERNAME = "username";
    public static final String USERS_PASSWORD = "password";
    public static final String USERS_WINS = "won_games";
    public static final String USERS_LOSSES = "lost_games";
    public static final String USERS_EMAIL = "email";
    public static final String USERS_SALT = "salt";
    public static final String USERS_TOTAL_GAMES = "total_games";
    public static final String USERS_RATIO = "ratio";
    public static final String USERS_LOGGED_IN = "logged_in";

    //Board table column names
    public static final String BOARDS_TABLE = "battleship_board";
    public static final String BOARDS_GAME_ID = "game_id";
    public static final String BOARDS_USER_ID = "user_id";
    public static final String BOARDS_COORDINATES = "coordinates";

    //Game table column names
    public static final String GAME_TABLE = "battleship_game";
    public static final String GAME_ID = "game_id";
    public static final String GAME_NAME = "name";
    public static final String GAME_HOST_ID = "host_id";
    public static final String GAME_JOIN_ID = "join_id";
    public static final String GAME_WINNER_ID = "winner_id";

    //Action table columns names
    public static final String ACTION_TABLE = "battleship_action";
    public static final String ACTION_GAME_ID = "game_id";
    public static final String ACTION_MOVE_ID = "move_id";
    public static final String ACTION_USER_ID = "user_id";
    public static final String ACTION_COORDINATES = "coordinates";

    //Feedback table column names
    public static final String FEEDBACK_TABLE = "battleship_feedback";
    public static final String FEEDBACK_TITLE = "feedback_title";
    public static final String FEEDBACK_MESSAGE = "feedback_message";
}
