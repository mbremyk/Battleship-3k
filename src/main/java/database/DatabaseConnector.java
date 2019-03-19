/**
 * DatabaseConnector.java
 * <p>
 * Handles all connections with the database
 * </p>
 */

package database;

import game.ShipCoordinates;
import model.BattleshipUser;

import static database.Constants.*;
import static database.Login.saltPassword;

import java.sql.*;
import java.util.Arrays;
import java.util.logging.*;

/**
 * DatabaseConnector class
 */
public class DatabaseConnector {
	private String databaseUrl;

	public DatabaseConnector(String databaseUrl) {
		this.databaseUrl = databaseUrl;
	}

	public boolean stringExistsInColumn(String string, String column, String table) {
		String query = "SELECT " + column + " FROM " + table + " WHERE " + column + " = ?";
		ResultSet res = null;
		try (Connection con = DriverManager.getConnection(databaseUrl);
		     PreparedStatement preparedStatement = con.prepareStatement(query);) {
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
					return new BattleshipUser(username, password, res.getString(USERS_EMAIL), res.getInt(USERS_WINS), res.getInt(USERS_LOSSES));
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
				newUsers[newUsers.length - 1] = new BattleshipUser(res.getString(USERS_USERNAME), res.getString(USERS_PASSWORD),
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
	public int[] lastAction(Game game){
		ResultSet res = null;
		String query = "SELECT * FROM " + ACTION + " WHERE " + ACTION_GAME_ID + " = " + game.getId() + " ORDER BY " + ACTION_MOVE_ID;
		try(Connection con = DriverManager.getConnection(databaseUrl)){

		}catch (SQLException e) {
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		finally{
			close(res);
		}
	}


	/**
	 * Method to get the ship's coordinates from the database
	 *
	 * @param gameid
	 * @param userid
	 * @return ShipCoordinates object containing information about the spaces the ships occupy
	 */
	public ShipCoordinates getShipCoordinates(int gameid, int userid) {
		ShipCoordinates coordinates = null;
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
				int[] coordX = new int[length];
				int[] coordY = new int[length];
				for (int i = 0; i < length; i++) {
					coordX[i] = Integer.parseInt(coordArray[i * 2]);
					coordY[i] = Integer.parseInt(coordArray[i * 2 + 1]);
				}
				coordinates = new ShipCoordinates(coordX, coordY);
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
}
