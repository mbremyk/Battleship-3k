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
    private static String DB_HOST = "";
    private static String DB_PORT = "";
    private static String DB_USER = "";
    private static String DB_PASSWORD = "";
    private static String DB_NAME = "";
    private static String DB_URL = "";

    //Database connection configuration
    public static void setDatabaseValues(String dbHost, String dbPort, String dbUser, String dbPassword, String dbName){
        DB_HOST = dbHost;
        DB_PORT = dbPort;
        DB_USER = dbUser;
        DB_PASSWORD = dbPassword;
        DB_NAME = dbName;
        DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?user=" + DB_USER + "&password=" + DB_PASSWORD;
    }
    /*
    private static String DB_HOST = "mysql.stud.idi.ntnu.no";
    private static String DB_PORT = "3306";
    private static String DB_USER = "thombje";
    private static String DB_PASSWORD = "TFWUfjmb";
    private static String DB_NAME = "thombje";
    private static String DB_URL = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?user=" + DB_USER + "&password=" + DB_PASSWORD;*/

    //Users table column names
    private static final String USERS_TABLE = "BattleshipUser";
    private static final String USERS_ID = "user_id";
    private static final String USERS_USERNAME = "username";
    private static final String USERS_PASSWORD = "password";
    private static final String USERS_WINS = "won_games";
    private static final String USERS_LOSSES = "lost_games";
    private static final String USERS_EMAIL = "email";
    private static final String USERS_SALT = "salt";
    private static final String USERS_TOTAL_GAMES = "total_games";
    private static final String USERS_RATIO = "ratio";
    private static final String USERS_LOGGED_IN = "logged_in";

    //Board table column names
    private static final String BOARDS_TABLE = "battleship_board";
    private static final String BOARDS_GAME_ID = "game_id";
    private static final String BOARDS_USER_ID = "user_id";
    private static final String BOARDS_COORDINATES = "coordinates";

    //Game table column names
    private static final String GAME_TABLE = "battleship_game";
    private static final String GAME_ID = "game_id";
    private static final String GAME_NAME = "name";
    private static final String GAME_HOST_ID = "host_id";
    private static final String GAME_JOIN_ID = "join_id";
    private static final String GAME_WINNER_ID = "winner_id";

    //Action table columns names
    private static final String ACTION_TABLE = "battleship_action.sql";
    private static final String ACTION_GAME_ID = "game_id";
    private static final String ACTION_MOVE_ID = "move_id";
    private static final String ACTION_USER_ID = "user_id";
    private static final String ACTION_COORDINATES = "coordinates";

    //Feedback table column names
    private static final String FEEDBACK_TABLE = "battleship_feedback";
    private static final String FEEDBACK_TITLE = "feedback_title";
    private static final String FEEDBACK_MESSAGE = "feedback_message";

    public static String getDbHost() {
        return DB_HOST;
    }

    public static String getDbPort() {
        return DB_PORT;
    }

    public static String getDbUser() {
        return DB_USER;
    }

    public static String getDbPassword() {
        return DB_PASSWORD;
    }

    public static String getDbName() {
        return DB_NAME;
    }

    public static String getDbUrl() {
        return DB_URL;
    }

    public static String getUsersTable() {
        return USERS_TABLE;
    }

    public static String getUsersId() {
        return USERS_ID;
    }

    public static String getUsersUsername() {
        return USERS_USERNAME;
    }

    public static String getUsersPassword() {
        return USERS_PASSWORD;
    }

    public static String getUsersWins() {
        return USERS_WINS;
    }

    public static String getUsersLosses() {
        return USERS_LOSSES;
    }

    public static String getUsersEmail() {
        return USERS_EMAIL;
    }

    public static String getUsersSalt() {
        return USERS_SALT;
    }

    public static String getUsersTotalGames() {
        return USERS_TOTAL_GAMES;
    }

    public static String getUsersRatio() {
        return USERS_RATIO;
    }

    public static String getUsersLoggedIn() {
        return USERS_LOGGED_IN;
    }

    public static String getBoardsTable() {
        return BOARDS_TABLE;
    }

    public static String getBoardsGameId() {
        return BOARDS_GAME_ID;
    }

    public static String getBoardsUserId() {
        return BOARDS_USER_ID;
    }

    public static String getBoardsCoordinates() {
        return BOARDS_COORDINATES;
    }

    public static String getGameTable() {
        return GAME_TABLE;
    }

    public static String getGameId() {
        return GAME_ID;
    }

    public static String getGameName() {
        return GAME_NAME;
    }

    public static String getGameHostId() {
        return GAME_HOST_ID;
    }

    public static String getGameJoinId() {
        return GAME_JOIN_ID;
    }

    public static String getGameWinnerId() {
        return GAME_WINNER_ID;
    }

    public static String getActionTable() {
        return ACTION_TABLE;
    }

    public static String getActionGameId() {
        return ACTION_GAME_ID;
    }

    public static String getActionMoveId() {
        return ACTION_MOVE_ID;
    }

    public static String getActionUserId() {
        return ACTION_USER_ID;
    }

    public static String getActionCoordinates() {
        return ACTION_COORDINATES;
    }

    public static String getFeedbackTable() {
        return FEEDBACK_TABLE;
    }

    public static String getFeedbackTitle() {
        return FEEDBACK_TITLE;
    }

    public static String getFeedbackMessage() {
        return FEEDBACK_MESSAGE;
    }
}
