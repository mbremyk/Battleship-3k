/**
 * DatabaseConnector.java
 *
 * <p>
 * Handles all connections with the database
 * </p>
 *
 * @author Brevik Magnus
 * @author Bjerke Thomas
 * @author Gulaker Kristian
 * @author Thorkildsen Torje
 * @author Granli Hans Kristian Olsen
 * @author Grande Trym
 */

package database;

import game.Game;
import game.Statics;
import model.BattleshipUser;

import static database.Constants.*;
import static database.Login.saltPassword;

import java.sql.*;
import java.util.Arrays;

public class DatabaseConnector {
    /**
     * The URL for the JDBC server
     */
    private String databaseUrl;

    /**
     * The ConnectionPool from which to get Connections
     */
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
     * @param game the game
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

    /**
     * Returns the game with the given host ID
     *
     * @param hostid ID of the player hosting the game
     * @return Game with the correct host ID
     */
    public Game getGame(int hostid) {
        return getGames(hostid)[0];
    }

    /**
     * Returns all the games from the database
     *
     * @return Array of Games
     */
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

    /**
     * Method for closing AutoClosables like ResultSets and PreparedStatements and handles their Exceptions
     *
     * @param closeable the AutoClosable to close
     */
    private void close(AutoCloseable closeable) {
        try {
            closeable.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * The same as {@link #close(AutoCloseable)}
     *
     * @param closeable1 the AutoClosable to close
     * @param closeable2 the AutoClosable to close
     */
    private void close(AutoCloseable closeable1, AutoCloseable closeable2) {
        try {
            closeable1.close();
            closeable2.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Deprecated
    /**
     * @param moveId ID of the move
     * @param gameId ID of the game
     * @return String representing the last coordinates affected
     * @deprecated made better methods
     */
    public String getLastCoordinates(int moveId, int gameId) {
        String coordinates = null;
        ResultSet res = null;
        PreparedStatement preparedStatement = null;
        String query = "SELECT * FROM " + ACTION_TABLE + " WHERE " + ACTION_GAME_ID + " = ?" + " AND " + ACTION_MOVE_ID + " = ?";
        Connection con = null;
        try {
            con = connectionPool.getConnection();
            res = preparedStatement.executeQuery();
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, gameId);
            preparedStatement.setInt(2, moveId);
            res = preparedStatement.executeQuery();
            connectionPool.releaseConnection(con);
            if (res.next()) {
                coordinates = res.getString(ACTION_COORDINATES);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        } finally {
            if (con != null) connectionPool.releaseConnection(con);
            if (res != null) close(res);
            if (preparedStatement != null) close(preparedStatement);
        }
        return coordinates;
    }

    /**
     * Checks if a user has joined the game
     *
     * @param game the Game for which to check if a user has joined
     * @return true if a user has joined, false if not
     */
    public boolean userJoined(Game game) {
        int gameId = game.getGameId();
        ResultSet res = null;
        PreparedStatement preparedStatement = null;
        String query = "SELECT bb." + BOARDS_USER_ID + "," + USERS_USERNAME + " FROM " + BOARDS_TABLE + " bb JOIN " + USERS_TABLE + " bu ON bb." + BOARDS_USER_ID + "=bu." + USERS_ID + " WHERE " + BOARDS_GAME_ID + " = " + gameId;
        Connection con = null;
        try {
            updateGameOver();
            con = connectionPool.getConnection();
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

    /**
     * Checks if there is a user in the given ResultSet for the given Game
     *
     * @param game Game to add user to
     * @param res  ResultSet to get information about a user from
     * @throws SQLException if something went wrong with the ResultSet
     */
    private void checkJoin(Game game, ResultSet res) throws SQLException {
        if (res.getInt(BOARDS_USER_ID) != Statics.getLocalUser().getUserId()) {
            BattleshipUser opponent = new BattleshipUser(res.getInt(BOARDS_USER_ID), res.getString(USERS_USERNAME));
            if (game.getJoinUser() == null) game.setJoinUser(opponent);
            //System.out.println("READY");
        }
    }

    /**
     * Uploads the coordinates of your ships using {@link #uploadShipCoordinates(int, int, String)}
     *
     * @param coordString String representing the coordinates of your ships "xx,yy,ww,hh,rrr"
     * @return true if the coordinates were uploaded, false if not, or an Exception was thrown
     */
    public boolean uploadShipCoordinates(String coordString) {
        Game game = Statics.getGame();
        BattleshipUser user = Statics.getLocalUser();
        if (game == null || user == null) return false;
        return uploadShipCoordinates(game.getGameId(), user.getUserId(), coordString);
    }

    /**
     * Uploads the coordinates of your ships
     *
     * @param gameid      ID of the game to which the coordinates are related
     * @param userid      ID of the user to which the coordinates are related
     * @param coordString String representing the coordinates of your ships "xx,yy,ww,hh,rrr"
     * @return true if the coordinates were uploaded, false if not, or an Exception was thrown
     */
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

    /**
     * Uploads an action to the database. This currently only involves shooting at a coordinate
     *
     * @param coordString the coordinates to shoot at in the form "xx,yy"
     * @return true if action was successfully uploaded, false if not, or if an Exception was thrown
     */
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

    /**
     * Creates a new game and uploads it to the database, after removing games hosted by the local player
     *
     * @param gameName The name of the game
     * @return true if game was successfully created and uploaded, false if not, or an Exception was thrown
     */
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
            con.setAutoCommit(false);
            deletePreparedStatement = con.prepareStatement(deleteQuery);
            insertPreparedStatement = con.prepareStatement(insertQuery);
            deletePreparedStatement.setInt(1, user.getUserId());
            deletePreparedStatement.execute();
            insertPreparedStatement.setInt(1, user.getUserId());
            insertPreparedStatement.setString(2, gameName);
            insertPreparedStatement.execute();
            con.commit();
            Statics.setGame(getGame(user.getUserId()));
            return true;
        } catch (Exception e1) {
            try {
                if (con != null) con.setAutoCommit(true);
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            e1.printStackTrace();
        } finally {
            if (con != null) connectionPool.releaseConnection(con);
            if (deletePreparedStatement != null) close(deletePreparedStatement);
            if (insertPreparedStatement != null) close(insertPreparedStatement);
        }
        return false;
    }

    /**
     * Setting the local user as the user that has joined the specific game
     *
     * @param game the Game to join
     * @return true if game was successfully joined, false if not, or if an Exception was thrown
     */
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

    /**
     * Uploads feedback to the database
     *
     * @param title   String representing the title of the feedback
     * @param message String with the message to the developers
     * @return true if feedback was successfully uploaded, false if not, or if an Exception was thrown
     */
    public boolean uploadFeedback(String title, String message) {
        if (title.equals("") || message.equals("")) {
            return false;
        } else {
            Connection con = null;
            PreparedStatement preparedStatement = null;
            try {
                con = connectionPool.getConnection();
                String update = "INSERT INTO " + FEEDBACK_TABLE + " VALUES (DEFAULT, ?, ?)";
                preparedStatement = con.prepareStatement(update);
                preparedStatement.setString(1, title);
                preparedStatement.setString(2, message);
                preparedStatement.execute();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            } finally {
                if (con != null) connectionPool.releaseConnection(con);
                if (preparedStatement != null) close(preparedStatement);
            }
        }
    }

    /**
     * Updates the score of the user. Adds 1 to either {@link Constants#USERS_WINS} or {@link Constants#USERS_LOSSES} depending on the results of the game
     *
     * @param userId     ID of the user to update the score of
     * @param gameResult the result of the game. 1 if the user won and 0 if the user lost
     * @return true if the result was successfully updated, false if not, or if an Exception was thrown
     */
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

    /**
     * Removes a game from the database
     *
     * @param game the Game to remove from the database
     * @return true if the Game was successfully removed, false if not, or if an Exception was thrown
     */
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

    /**
     * Logs the local user out of the database. This has no current function, other than changing the USERS_LOGGED_IN column in the database
     *
     * @return true if the user was successfully logged out, false if not, or if an Exception was thrown
     */
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

    /**
     * Uploads the results of the local game. Sets Constants.GAME_WINNER_ID column to the ID of the winner of the current Game
     *
     * @return true if the results were successfully uploaded, false if not, or if an Exception was thrown
     */
    public boolean uploadResults() {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        String query = "UPDATE " + GAME_TABLE + " SET " + GAME_WINNER_ID + " = ? WHERE " + GAME_ID + " = ?";
        try {
            con = connectionPool.getConnection();
            preparedStatement = con.prepareStatement(query);
            if (Statics.getGame().getWinner() != null) {
                preparedStatement.setInt(1, Statics.getGame().getWinner().getUserId());
            } else {
                return false;
            }
            preparedStatement.setInt(2, Statics.getGame().getGameId());
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

    /**
     * Checks if there is a winner of the current Game in the database and updates the local Game object
     *
     * @throws Exception if something went wrong. WIIUUU WIIUUU WIIUUU
     */
    public void updateGameOver() throws Exception {
        Connection con = null;
        PreparedStatement preparedStatement = null;
        ResultSet res = null;
        String query = "SELECT " + GAME_WINNER_ID + " FROM " + GAME_TABLE + " WHERE " + GAME_ID + " = ?";
        try {
            con = connectionPool.getConnection();
            preparedStatement = con.prepareStatement(query);
            preparedStatement.setInt(1, Statics.getGame().getGameId());
            res = preparedStatement.executeQuery();
            if (res.next() && res.getInt(GAME_WINNER_ID) != 0) {
                Statics.getGame().setGameOver(true);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Could not get game over state");
        } finally {
            if (res != null) close(res);
            if (preparedStatement != null) close(preparedStatement);
            if (con != null) connectionPool.releaseConnection(con);
        }
    }
}
