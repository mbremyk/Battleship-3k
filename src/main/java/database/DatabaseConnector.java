/**
 * DatabaseConnector.java
 * <p>
 * Handles all connections with the database
 * </p>
 */

package database;

import game.Game;
import model.BattleshipUser;

import static database.Constants.*;
import static database.Login.saltPassword;

import java.sql.*;
import java.util.Arrays;
import java.util.logging.*;

public class DatabaseConnector {

	private String databaseUrl;

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
		try (Connection con = DriverManager.getConnection(databaseUrl);
		     PreparedStatement preparedStatement = con.prepareStatement(query)) {
			preparedStatement.setString(1, string);
			res = preparedStatement.executeQuery();
			if (res.next()) {
				return true;
			}
		}
		catch (SQLException e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		catch (Exception e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		finally {
			close(res);
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
		try (Connection con = DriverManager.getConnection(databaseUrl);
		     PreparedStatement preparedStatement = con.prepareStatement(query)) {
			preparedStatement.setString(1, username);
			preparedStatement.setBytes(2, hashedPassword);
			preparedStatement.setString(3, email);
			preparedStatement.setBytes(4, salt);
			preparedStatement.execute();
			return true;
		}
		catch (SQLException e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		catch (Exception e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
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
		try (Connection con = DriverManager.getConnection(databaseUrl);
		     PreparedStatement preparedStatement = con.prepareStatement(query)) {
			preparedStatement.setString(1, username);
			res = preparedStatement.executeQuery();
			if (res.next()) {
				byte[] passwordHash = res.getBytes(USERS_PASSWORD);
				byte[] salt = res.getBytes(USERS_SALT);
				if (Arrays.equals(saltPassword(password, salt), passwordHash))  //password.equals(res.getString("password") for unhashed passwords
				{
					return new BattleshipUser(res.getInt(USERS_ID), username, password, res.getString(USERS_EMAIL), res.getInt(USERS_WINS), res.getInt(USERS_LOSSES));
				}
			}
		}
		catch (SQLException e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		catch (Exception e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		finally {
			close(res);
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
		try (Connection con = DriverManager.getConnection(databaseUrl);
		     PreparedStatement preparedStatement = con.prepareStatement(query)) {
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
		}
		catch (SQLException e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		finally {
			close(res);
		}
		return null;
	}

	public int lastAction(Game game) {
		int moveId = 0;
		ResultSet res = null;
		String query = "SELECT * FROM " + ACTION_TABLE + " WHERE " + ACTION_GAME_ID + " = " + game.getGameId() + " ORDER BY " + ACTION_MOVE_ID;
		try (Connection con = DriverManager.getConnection(databaseUrl);
		     PreparedStatement prepareStatement = con.prepareStatement(query)) {
			if (res.next()) {
				moveId = res.getInt(ACTION_MOVE_ID);
			}
		}
		catch (Exception e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		finally {
			close(res);
		}
		return moveId;
	}

	/**
	 * Method to get the ship's coordinates from the database
	 *
	 * @param gameid the autogenerated id of the relevant game
	 * @param userid the autogenerated id of the relevant user
	 * @return two-dimensional array object containing information about the spaces the ships occupy
	 */
	public int[][] getShipCoordinates(int gameid, int userid) {
		int[][] coordinates = null;
		ResultSet res = null;
		String query = "SELECT " + BOARDS_COORDINATES + " FROM " + BOARDS_TABLE + " WHERE " + BOARDS_GAME_ID + "=" + "? AND " + BOARDS_USER_ID + "= ?";

		try (Connection con = DriverManager.getConnection(databaseUrl);
		     PreparedStatement preparedStatement = con.prepareStatement(query)) {

			preparedStatement.setString(1, gameid + "");
			preparedStatement.setString(2, userid + "");
			res = preparedStatement.executeQuery();

			if (res.next()) {
				String coordString = res.getString(BOARDS_COORDINATES);
				String[] coordArray = coordString.split(",");

				int length = coordArray.length / 2;
				coordinates = new int[length][2];
				for (int i = 0; i < length; i++) {
					coordinates[i][0] = Integer.parseInt(coordArray[i * 2]);
					coordinates[i][1] = Integer.parseInt(coordArray[i * 2 + 1]);
				}
			}
		}
		catch (SQLException e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		finally {
			close(res);
		}

		return coordinates;
	}

	private void close(AutoCloseable closeable) {
		try {
			closeable.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void close(AutoCloseable closeable1, AutoCloseable closeable2) {
		try {
			closeable1.close();
			closeable2.close();
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getLastCoordinates(int moveId, int gameId){
		String coordinates = "";
    	ResultSet res = null;
		String query = "SELECT * FROM " + ACTION_TABLE + " WHERE " + ACTION_GAME_ID + " = " + gameId + " AND " + ACTION_MOVE_ID + " = " + moveId;
		try(Connection con = DriverManager.getConnection(databaseUrl)){
			PreparedStatement prepareStatement = con.prepareStatement(query);
			if(res.next()){
				coordinates = res.getString(ACTION_COORDINATES);
			}
		}catch (SQLException e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		return coordinates;
	}
	public boolean userJoined(Game game){
		int gameId = game.getGameId();
		ResultSet res = null;
		String query = "SELECT " + BOARDS_USER_ID + " FROM " + BOARDS_TABLE + " WHERE " + BOARDS_GAME_ID + " = " + gameId;
		try(Connection con = DriverManager.getConnection(databaseUrl)){
			PreparedStatement preparedStatement = con.prepareStatement(query);
			 int users = res.getInt(BOARDS_USER_ID);
			if(res.next() && res.next()) {
				System.out.println("READY");
				return true;
			}else{
                System.out.println("NOT READY");
			}
		}catch (Exception e){
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
	return false;
	}
}
