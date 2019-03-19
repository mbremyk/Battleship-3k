/**
 * DatabaseConnector.java
 * <p>
 * Handles all connections with the database
 * </p>
 */

package database;

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
	
	/**
	 * Checks if string 'string' exists in 'column' in 'table'
	 *
	 * @param string String to search for
	 * @param column String containing the column to search in
	 * @param table  String
	 * @return
	 */
	public boolean stringExistsInColumn(String string, String column, String table) {
		try (Connection con = DriverManager.getConnection(databaseUrl)) {
			String query = "SELECT " + column + " FROM " + table + " WHERE " + column + " = ?";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, string);
			ResultSet res = preparedStatement.executeQuery();
			
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
		return false;
	}
	
	/**
	 * Method for registering users in the database
	 *
	 * @param username username to register. This will be checked for other occurrences in the database
	 * @param hashedPassword the hashed password of the user
	 * @param email the user's email address
	 * @param salt the secure randomly generated salt that was used to hash the password
	 * @return true if registration was successful, false if not
	 */
	public boolean registerUser(String username, byte[] hashedPassword, String email, byte[] salt) {
		try (Connection con = DriverManager.getConnection(databaseUrl)) {
			String query = "INSERT INTO " + USERS_TABLE + "(" + USERS_USERNAME + "," + USERS_PASSWORD + "," + USERS_EMAIL + "," + USERS_SALT + ") VALUES(?,?,?,?)";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setBytes(2, hashedPassword);
			preparedStatement.setString(3, email);
			preparedStatement.setBytes(4, salt);
			preparedStatement.execute();
			return true;
		}
		catch (SQLException e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
			return false;
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
		try (Connection con = DriverManager.getConnection(databaseUrl)) {
			String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + USERS_USERNAME + " = ?";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, username);
			ResultSet res = preparedStatement.executeQuery();
			if (res.next()) {
				byte[] passwordHash = res.getBytes(USERS_PASSWORD);
				byte[] salt = res.getBytes(USERS_SALT);
				if (Arrays.equals(saltPassword(password, salt), passwordHash))  //password.equals(res.getString("password") for unhashed passwords
				{
					return new BattleshipUser(username, passwordHash, res.getString(USERS_EMAIL), res.getInt(USERS_WINS), res.getInt(USERS_LOSSES));
				}
			}
		}
		catch (SQLException e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		catch (Exception e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		return null;
	}
	
	/**
	 * Method for fetching all users from the database
	 *
	 * @return Array of BattleshipUser containing all BattleshipUser in the database
	 */
	public BattleshipUser[] getBattleshipUsers() {
		try (Connection con = DriverManager.getConnection(databaseUrl)) {
			BattleshipUser[] users = new BattleshipUser[0];
			PreparedStatement setning = con.prepareStatement("SELECT * FROM " + USERS_TABLE + "");
			ResultSet res = setning.executeQuery();
			
			while (res.next()) {
				BattleshipUser[] newUsers = new BattleshipUser[users.length + 1];
				for (int i = 0; i < users.length; i++) {
					newUsers[i] = users[i];
				}
				newUsers[newUsers.length - 1] = new BattleshipUser(res.getString(USERS_USERNAME), res.getBytes(USERS_PASSWORD),
						res.getString(USERS_EMAIL), res.getInt(USERS_WINS), res.getInt(USERS_LOSSES));
				users = newUsers;
			}
			return users;
		}
		catch (SQLException e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		return null;
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
		
		try (Connection con = DriverManager.getConnection(databaseUrl)) {
			PreparedStatement preparedStatement = con.prepareStatement("SELECT " + BOARDS_COORDINATES + " FROM " + BOARDS_TABLE + " WHERE " + BOARDS_GAME_ID + "=" + "? AND " + BOARDS_USER_ID + "=?");
			preparedStatement.setString(1, gameid + "");
			preparedStatement.setString(2, userid + "");
			ResultSet res = preparedStatement.executeQuery();
			
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
		
		return coordinates;
	}
}
