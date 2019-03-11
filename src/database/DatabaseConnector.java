/**
 * DatabaseConnector.java
 * <p>
 * Handles all connections with the database
 */

package database;

import model.BattleshipUser;

import static database.Constants.*;
import static database.Login.saltPassword;

import java.sql.*;
import java.util.Arrays;
import java.util.logging.*;

public class DatabaseConnector
{
	private String databaseUrl;
	
	public DatabaseConnector(String databaseUrl)
	{
		this.databaseUrl = databaseUrl;
	}
	
	public boolean stringExistsInColumn(String string, String column, String table)
	{
		try (Connection con = DriverManager.getConnection(databaseUrl))
		{
			String query = "SELECT " + column + " FROM " + table + " WHERE " + column + " = ?";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, string);
			ResultSet res = preparedStatement.executeQuery();
			
			if (res.next())
			{
				return true;
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		catch (Exception e)
		{
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		return false;
	}
	
	public boolean registerUser(String username, byte[] hashedPassword, String email, byte[] salt)
	{
		try (Connection con = DriverManager.getConnection(databaseUrl))
		{
			String query = "INSERT INTO " + USERS_TABLE + "(" + USERS_USERNAME + "," + USERS_PASSWORD + "," + USERS_EMAIL + "," + USERS_SALT + ") VALUES(?,?,?,?)";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, username);
			preparedStatement.setBytes(2, hashedPassword);
			preparedStatement.setString(3, email);
			preparedStatement.setBytes(4, salt);
			preparedStatement.execute();
			return true;
		}
		catch (SQLException e)
		{
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
			return false;
		}
		catch (Exception e)
		{
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		return false;
	}
	
	public BattleshipUser getBattleshipUser(String username, String password)
	{
		try (Connection con = DriverManager.getConnection(databaseUrl))
		{
			String query = "SELECT * FROM " + USERS_TABLE + " WHERE " + USERS_USERNAME + " = ?";
			PreparedStatement preparedStatement = con.prepareStatement(query);
			preparedStatement.setString(1, username);
			ResultSet res = preparedStatement.executeQuery();
			if (res.next())
			{
				byte[] passwordHash = res.getBytes(USERS_PASSWORD);
				byte[] salt = res.getBytes(USERS_SALT);
				if (Arrays.equals(saltPassword(password, salt), passwordHash))  //password.equals(res.getString("password") for unhashed passwords
				{
					return new BattleshipUser(username, password, res.getString(USERS_EMAIL), res.getInt(USERS_WINS), res.getInt(USERS_LOSSES));
				}
			}
		}
		catch (SQLException e)
		{
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		catch (Exception e)
		{
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		return null;
	}
}
