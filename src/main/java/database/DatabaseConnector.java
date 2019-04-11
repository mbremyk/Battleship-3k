/**
 * DatabaseConnector.java
 * @Author Brevik Magnus
 * @Author Bjerke Thomas
 * @Author Gulaker Kristian
 * @Author Thorkildsen Torje
 * @Author Granli Hans Kristian Olsen
 * @Author Grande Trym
 * <p>
 * Handles all connections with the database
 * </p>
 */

package database;

import game.Game;
import game.Statics;
import model.BattleshipUser;

import static database.Constants.*;
import static database.Login.saltPassword;

import java.sql.*;
import java.util.Arrays;
import java.util.logging.*;

public class DatabaseConnector {
    private String databaseUrl;
    private static ConnectionPool connectionPool;


    /**
     * Constructor for the DatabaseConnector class
     *
     * @param databaseUrl String containing the URL of the database, with username and password included
     */
    public DatabaseConnector(String databaseUrl) {
        this.databaseUrl = databaseUrl;
    }

    public DatabaseConnector() {
        this.databaseUrl = Constants.DB_URL;
    }

    public ConnectionPool setConnectionPool(ConnectionPool _connectionPool) {
        connectionPool = _connectionPool;
        return connectionPool;
    }

    /**
     * Checks if string 'string' exists in 'column' in 'table'
     *
     * @param string String to search for
     * @param column String containing the column to search in
     * @param table  String
     * @return true if string exists, false if not
     */
    public boolean stringExistsInColumn(String string, String column, String table) {
        String query = "SELECT " + column + " FROM " + table + " WHERE " + column + " = ?";
        ResultSet res = null;
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = connectionPool.getConnection();
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, string);
            res = preparedStatement.executeQuery();
            if (res.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) close(res);
            if (preparedStatement != null) close(preparedStatement);
            if (con != null) connectionPool.releaseConnection(con);
        }
        return false;
    }


    /**
     * Method for registering users in the database
     *
     * @param username       username to register. This will be checked for other occurrences in the database
     * @param hashedPassword the hashed password of the user
     * @param email          the user's email address
     * @param salt           the secure randomly generated salt that was used to hash the password
     * @return true if registration was successful, false if not
     */
    public boolean registerUser(String username, byte[] hashedPassword, String email, byte[] salt) {
        String query = "INSERT INTO " + USERS_TABLE + "(" + USERS_USERNAME + "," + USERS_PASSWORD + "," + USERS_EMAIL + "," + USERS_SALT + ") VALUES(?,?,?,?)";
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = connectionPool.getConnection();
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);
            preparedStatement.setBytes(2, hashedPassword);
            preparedStatement.setString(3, email);
            preparedStatement.setBytes(4, salt);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (preparedStatement != null) close(preparedStatement);
            if (con != null) connectionPool.releaseConnection(con);
        }
        return false;
    }

    /**
     * Method for fetching information about a specific user, given username and password
     *
     * @param username the username to look for
     * @param password the unhashed password given by the user
     * @return BattleshipUser object with information about the user, or null if the get was unsuccessful
     */
    public BattleshipUser getBattleshipUser(String username, String password) {
        ResultSet res = null;
        String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + USERS_USERNAME + " = ?";
        String loginQuery = "UPDATE " + USERS_TABLE + " SET " + USERS_LOGGED_IN + " = 1 WHERE " + USERS_ID + " = ?";
        Connection con = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement loginPreparedStatement = null;
        try {
            con = connectionPool.getConnection();
            con.setAutoCommit(false);
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);
            res = preparedStatement.executeQuery();
            BattleshipUser out = null;
            if (res.next()) {
                byte[] passwordHash = res.getBytes(USERS_PASSWORD);
                byte[] salt = res.getBytes(USERS_SALT);
                if (Arrays.equals(saltPassword(password, salt), passwordHash))  //password.equals(res.getString("password") for unhashed passwords
                {
                    out = new BattleshipUser(res.getInt(USERS_ID), username, password, res.getString(USERS_EMAIL), res.getInt(USERS_WINS), res.getInt(USERS_LOSSES));
                    loginPreparedStatement = con.prepareStatement(loginQuery);
                    loginPreparedStatement.setInt(1, out.getUserId());
                    loginPreparedStatement.execute();
                }
            }
            con.commit();
            return out;
        } catch (Exception e1) {
            try {
                con.rollback();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            e1.printStackTrace();
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (Exception e3) {
                e3.printStackTrace();
            }
            if (res != null) close(res);
            if (preparedStatement != null) close(preparedStatement);
            if (con != null) connectionPool.releaseConnection(con);
        }
        return null;
    }


    /**
     * Method for fetching all users from the database
     *
     * @return Array of BattleshipUser containing all BattleshipUser in the database
     */
    public BattleshipUser[] getBattleshipUsers() {
        ResultSet res = null;
        String query = "SELECT * FROM " + USERS_TABLE;
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = connectionPool.getConnection();
            preparedStatement = con.prepareStatement(query);
            BattleshipUser[] users = new BattleshipUser[0];
            res = preparedStatement.executeQuery();

            while (res.next()) {
                BattleshipUser[] newUsers = new BattleshipUser[users.length + 1];
                for (int i = 0; i < users.length; i++) {
                    newUsers[i] = users[i];
                }
                newUsers[newUsers.length - 1] = new BattleshipUser(res.getInt(USERS_ID), res.getString(USERS_USERNAME), res.getString(USERS_PASSWORD),
                        res.getString(USERS_EMAIL), res.getInt(USERS_WINS), res.getInt(USERS_LOSSES));
                users = newUsers;
            }
            return users;
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (res != null) close(res);
            if (preparedStatement != null) close(preparedStatement);
            if (con != null) connectionPool.releaseConnection(con);
        }
        return null;
    }

    /**
     * Loads the latest attacks from the opponent into a cache
     *
     * @param game
     * @return latest move_id
     */
    public int lastAction(Game game) {
        int moveId = game.getMoveId();
        ResultSet res = null;
        PreparedStatement preparedStatement = null;
        PreparedStatement deletePreparedStatement = null;
        Connection con = null;
        try {
            con = connectionPool.getConnection();
            con.setAutoCommit(false);
            String query = "SELECT * FROM " + ACTION_TABLE + " WHERE " + ACTION_GAME_ID + " = ? " + " AND " + ACTION_USER_ID + " != ?" + " AND " + ACTION_MOVE_ID + " > ?" + " ORDER BY " + ACTION_MOVE_ID;
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, game.getGameId());
            preparedStatement.setInt(2, Statics.getLocalUser().getUserId());
            preparedStatement.setInt(3, moveId);
            res = preparedStatement.executeQuery();
            while (res.next()) {
                game.addCachedAction(res.getString(ACTION_COORDINATES) + "," + res.getString(ACTION_MOVE_ID));
            }
            String deleteQuery = "DELETE FROM " + ACTION_TABLE + " WHERE " + ACTION_GAME_ID + " = ? " + " AND " + ACTION_MOVE_ID + " < ?";
            deletePreparedStatement = con.prepareStatement(deleteQuery);
            deletePreparedStatement.setInt(1, game.getGameId());
            deletePreparedStatement.setInt(2, moveId);
            deletePreparedStatement.execute();

            con.commit();
        } catch (Exception e) {
            try {
                con.rollback();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
            e.printStackTrace();
            return -1;
        } finally {
            try {
                con.setAutoCommit(true);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            if (res != null) close(res);
            if (preparedStatement != null) close(preparedStatement);
            if (deletePreparedStatement != null) close(deletePreparedStatement);
            if (con != null) connectionPool.releaseConnection(con);
        }
        return moveId;
    }

    public Game getGame(int hostid) {
        return getGames(hostid)[0];
    }

    public Game[] getGames() {
        return getGames(-1);
    }

    /**
     * Method for retrieving all current games from database
     *
     * @return table of Game objects created from database
     */
    public Game[] getGames(int hostid) {
        Game[] games;
        String query = "SELECT " + USERS_TABLE + "." + USERS_USERNAME + "," + USERS_TABLE + "." + USERS_WINS + "," + GAME_TABLE + "." + GAME_ID + ","
                + GAME_TABLE + "." + GAME_HOST_ID + "," + "" + GAME_TABLE + "." + GAME_JOIN_ID + ","
                + GAME_TABLE + "." + GAME_NAME + " FROM " + USERS_TABLE + "" +
                " INNER JOIN " + GAME_TABLE + " ON " + GAME_TABLE + "." + GAME_HOST_ID + " = " + USERS_TABLE + "." + USERS_ID;

        if (hostid != -1) query += " WHERE " + GAME_HOST_ID + " = ?";

        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet res = null;
        try {
            con = connectionPool.getConnection();
            preparedStatement = con.prepareStatement(query);
            if (hostid != -1) preparedStatement.setInt(1, hostid);
            res = preparedStatement.executeQuery();

            games = new Game[0];

            while (res.next()) {
                int hostId = res.getInt(GAME_HOST_ID);
                String username = res.getString(USERS_USERNAME);
                int hostWins = res.getInt(USERS_WINS);
                BattleshipUser newHost = new BattleshipUser(hostId, username, "", "", hostWins, 0);
                BattleshipUser newJoin;
                Game newGame = new Game(res.getInt(GAME_ID), res.getString(GAME_NAME), newHost, hostid != -1);
                if (res.getString(GAME_JOIN_ID) == null) {
                    newJoin = null;
                    newGame.setGameOpen(true);
                } else {
                    newJoin = new BattleshipUser(res.getInt(GAME_JOIN_ID), "", "", "");
                    newGame.setGameOpen(false);
                }
                newGame.setJoinUser(newJoin);
                Game[] newGames = new Game[games.length + 1];
                for (int i = 0; i < games.length; i++) {
                    newGames[i] = games[i];
                }
                newGames[newGames.length - 1] = newGame;
                games = newGames;

            }
            return games;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (res != null) close(res);
            if (preparedStatement != null) close(preparedStatement);
            if (con != null) connectionPool.releaseConnection(con);
        }
        return null;
    }

    /**
     * Method to get the ship's coordinates from the database
     *
     * @param gameid the autogenerated id of the relevant game
     * @param userid the autogenerated id of the relevant user
     * @return
     */
    public String getShipCoordinatesString(int gameid, int userid) {
        ResultSet res = null;
        String query = "SELECT " + BOARDS_COORDINATES + " FROM " + BOARDS_TABLE + " WHERE " + BOARDS_GAME_ID + "=" + "? AND " + BOARDS_USER_ID + "= ?";
        String coordString = null;
        PreparedStatement preparedStatement = null;
        Connection con = null;
        try {
            con = connectionPool.getConnection();
            if (con == null) {
                System.out.println("OUT OF CONNECTIONS");
                return null;
            }
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, gameid);
            preparedStatement.setInt(2, userid);
            res = preparedStatement.executeQuery();

            if (res.next()) {
                coordString = res.getString(BOARDS_COORDINATES);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (res != null) close(res);
            if (preparedStatement != null) close(preparedStatement);
            if (con != null) connectionPool.releaseConnection(con);
        }

        return coordString;
    }

    private void close(AutoCloseable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void close(AutoCloseable closeable1, AutoCloseable closeable2) {
        try {
            closeable1.close();
            closeable2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    public String getLastCoordinates(int moveId, int gameId) {
//        String coordinates = null;
//        ResultSet res = null;
//        PreparedStatement preparedStatement = null;
//        String query = "SELECT * FROM " + ACTION_TABLE + " WHERE " + ACTION_GAME_ID + " = ?" + " AND " + ACTION_MOVE_ID + " = ?";
//        Connection con = null;
//        try {
//            con = connectionPool.getConnection();
//            res = preparedStatement.executeQuery();
//            preparedStatement = con.prepareStatement(query);
//            preparedStatement.setInt(1, gameId);
//            preparedStatement.setInt(2, moveId);
//            res = preparedStatement.executeQuery();
//            connectionPool.releaseConnection(con);
//            if (res.next()) {
//                coordinates = res.getString(ACTION_COORDINATES);
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//            return null;
//        } finally {
//            if (con != null) connectionPool.releaseConnection(con);
//            if (res != null) close(res);
//            if (preparedStatement != null) close(preparedStatement);
//        }
//        return coordinates;
//    }

    public boolean userJoined(Game game) {
        //TODO clean up this code
        int gameId = game.getGameId();
        ResultSet res = null;
        PreparedStatement preparedStatement = null;
        String query = "SELECT bb." + BOARDS_USER_ID + "," + USERS_USERNAME + " FROM " + BOARDS_TABLE + " bb JOIN " + USERS_TABLE + " bu ON bb." + BOARDS_USER_ID + "=bu." + USERS_ID + " WHERE " + BOARDS_GAME_ID + " = " + gameId;
        Connection con = null;
        try {
            con = connectionPool.getConnection();
            if (con == null) return false;
            preparedStatement = con.prepareStatement(query);
            res = preparedStatement.executeQuery();
            if (res.next()) {
                checkJoin(game, res);
                if (res.next()) {
                    checkJoin(game, res);
                    return true;
                }
            } else {
//                System.out.println("NOT READY");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) connectionPool.releaseConnection(con);
            if (res != null) close(res);
            if (preparedStatement != null) close(preparedStatement);
        }
        return false;
    }

    private void checkJoin(Game game, ResultSet res) throws SQLException {
        if (res.getInt(BOARDS_USER_ID) != Statics.getLocalUser().getUserId()) {
            BattleshipUser opponent = new BattleshipUser(res.getInt(BOARDS_USER_ID), res.getString(USERS_USERNAME));
            if (game.getJoinUser() == null) game.setJoinUser(opponent);
            System.out.println("READY");
        }
    }

    public boolean uploadShipCoordinates(String coordString) {
        Game game = Statics.getGame();
        BattleshipUser user = Statics.getLocalUser();
        if (game == null || user == null) return false;
        return uploadShipCoordinates(game.getGameId(), user.getUserId(), coordString);
    }

    public boolean uploadShipCoordinates(int gameid, int userid, String coordString) {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        try {
            con = connectionPool.getConnection();
            String query = "INSERT INTO " + BOARDS_TABLE + "(" + BOARDS_GAME_ID + "," + BOARDS_USER_ID + "," + BOARDS_COORDINATES + ")VALUES(?,?,?)";
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, gameid);
            preparedStatement.setInt(2, userid);
            preparedStatement.setString(3, coordString);
            preparedStatement.execute();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) connectionPool.releaseConnection(con);
            if (preparedStatement != null) close(preparedStatement);
        }
        return false;
    }

    public boolean doAction(String coordString) {
        Game game = Statics.getGame();
        int gameId = game.getGameId();
        int moveId = game.getMoveId();
        Connection con = null;
        PreparedStatement insertPreparedStatement = null;
        try {
            con = connectionPool.getConnection();
            String insertQuery = "INSERT INTO " + ACTION_TABLE + "(" + ACTION_GAME_ID + "," + ACTION_MOVE_ID + "," + ACTION_USER_ID + "," + ACTION_COORDINATES + ")VALUES(?,?,?,?)";
            insertPreparedStatement = con.prepareStatement(insertQuery);
            insertPreparedStatement.setInt(1, gameId);
            insertPreparedStatement.setInt(2, moveId + 1);
            insertPreparedStatement.setInt(3, Statics.getLocalUser().getUserId());
            insertPreparedStatement.setString(4, coordString);
            insertPreparedStatement.execute();
            game.incMoveID();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) connectionPool.releaseConnection(con);
            if (insertPreparedStatement != null) close(insertPreparedStatement);
        }
        return false;
    }

    public boolean createGame(String gameName) {
        BattleshipUser user = Statics.getLocalUser();
        if (user == null) return false;
        String deleteQuery = "DELETE FROM " + GAME_TABLE + " WHERE " + GAME_HOST_ID + " = ? ";
        String insertQuery = "INSERT INTO " + GAME_TABLE + "(" + GAME_HOST_ID + "," + GAME_NAME + ") VALUES(?,?)";
        Connection con = null;
        PreparedStatement deletePreparedStatement = null;
        PreparedStatement insertPreparedStatement = null;
        try {
            con = connectionPool.getConnection();
            deletePreparedStatement = con.prepareStatement(deleteQuery);
            insertPreparedStatement = con.prepareStatement(insertQuery);
            deletePreparedStatement.setInt(1, user.getUserId());
            deletePreparedStatement.execute();
            insertPreparedStatement.setInt(1, user.getUserId());
            insertPreparedStatement.setString(2, gameName);
            insertPreparedStatement.execute();
            Statics.setGame(getGame(user.getUserId()));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) connectionPool.releaseConnection(con);
            if (deletePreparedStatement != null) close(deletePreparedStatement);
            if (insertPreparedStatement != null) close(insertPreparedStatement);
        }
        return false;
    }

    public boolean joinGame(Game game) {
        BattleshipUser user = Statics.getLocalUser();
        if (user == null) return false;
        String query = "UPDATE " + GAME_TABLE + " SET " + GAME_JOIN_ID + " = ? WHERE " + GAME_ID + " = " + game.getGameId() + " AND " + GAME_JOIN_ID + " IS NULL";
        PreparedStatement preparedStatement = null;
        Connection con = null;
        try {
            con = connectionPool.getConnection();
            if (con == null) return false;
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, user.getUserId());
            preparedStatement.execute();
            game.setJoinUser(user);
            Statics.setGame(game);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (con != null) connectionPool.releaseConnection(con);
            if (preparedStatement != null) close(preparedStatement);
        }
        return false;
    }


    public boolean uploadFeedback(String title, String message) {
        if (title == "" || message == "" || title == null || message == null) {
            return false;
        } else {
            Connection con = null;
            try {
                con = connectionPool.getConnection();
                String update = "INSERT INTO " + FEEDBACK_TABLE + " VALUES (DEFAULT, ?, ?)";
                PreparedStatement preparedStatement = con.prepareStatement(update);
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, message);
                preparedStatement.execute();
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (con != null) connectionPool.releaseConnection(con);
            }
        }
    }

    public boolean updateUserScore(int userId, int gameResult) {
        Connection con = null;
        PreparedStatement statement = null;
        String query;
//        ResultSet res;
//        int currentValue = 0;
        String column;
        try {
            con = connectionPool.getConnection();
            if (gameResult == 1) {
//                query = "SELECT " + USERS_WINS + " FROM " + USERS_TABLE + "";
                column = USERS_WINS;
            } else {
//                query = "SELECT " + USERS_LOSSES + " FROM " + USERS_TABLE + "";
                column = USERS_LOSSES;
//            }
//            statement = con.prepareStatement(query);
//            res = statement.executeQuery();
//            if(res.next()){
//                currentValue = res.getInt(column);
            }
            query = "UPDATE " + USERS_TABLE + " SET " + column + " = " + column + "+1" + " WHERE " + USERS_ID + " = ?";
            statement = con.prepareStatement(query);
            statement.setInt(1, userId);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) connectionPool.releaseConnection(con);
            if (statement != null) close(statement);
        }
    }

    public boolean removeGameFromDatabase(Game game) {
        Connection con = null;
        PreparedStatement statement = null;
        String query;
        ResultSet res;
        try {
            con = connectionPool.getConnection();
            query = "DELETE FROM " + GAME_TABLE + " WHERE " + GAME_ID + " = '" + game.getGameId() + "'";
            statement = con.prepareStatement(query);
            statement.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (con != null) connectionPool.releaseConnection(con);
            if (statement != null) close(statement);
        }
    }

    public boolean logout() {
        if (Statics.getLocalUser() != null) {
            Connection con = null;
            PreparedStatement preparedStatement = null;
            String query = "UPDATE " + USERS_TABLE + " SET " + USERS_LOGGED_IN + " = ? WHERE " + USERS_ID + " = ?";
            try {
                con = connectionPool.getConnection();
                preparedStatement = con.prepareStatement(query);
                preparedStatement.setBoolean(1, false);
                preparedStatement.setInt(2, Statics.getLocalUser().getUserId());
                preparedStatement.execute();
                Statics.setLocalUser(null);
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (con != null)
                    connectionPool.releaseConnection(con);
                if (preparedStatement != null)
                    close(preparedStatement);
            }
        }
        return true;
    }

    public boolean uploadResults() {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        String query = "UPDATE " + GAME_TABLE + " SET " + GAME_WINNER_ID + " = ? WHERE " + GAME_ID + " = ?";
        try {
            con = connectionPool.getConnection();
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, Statics.getGame().getGameId());
            if (Statics.getGame().getWinner() != null) {
                preparedStatement.setInt(2, Statics.getGame().getWinner().getUserId());
            } else {
                return false;
            }
            preparedStatement.execute();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (con != null)
                connectionPool.releaseConnection(con);
            if (preparedStatement != null)
                close(preparedStatement);
        }
        return true;
    }
}
