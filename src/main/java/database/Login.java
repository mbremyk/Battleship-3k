/**
 * database.Login.java
 *
 * <p>
 * Handles registering of new users, login of existing users and salting and hashing passwords
 * <p>
 *
 * @author Brevik Magnus
 * @author Bjerke Thomas
 * @author Gulaker Kristian
 * @author Thorkildsen Torje
 */

package database;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.logging.Level;
import java.util.logging.Logger;

import game.Statics;
import model.*;

import static database.Constants.*;

/**
 * Login class
 */
public abstract class Login
{
	
	private static String databaseUrl = Constants.DB_URL;
	private static final DatabaseConnector databaseConnector = new DatabaseConnector(databaseUrl);
	
	/**
	 * Handles registering of a new user with the desired username, password, and email.
	 * Hashes password using the generateSalt() and saltPassword() functions
	 *
	 * @param username String containing the desired username
	 * @param password String containing the desired password
	 * @param email    String containing the desired email address
	 * @return boolean representing whether the registering of a new user was successful or not. Returns true if user was registered, and false if a user with either the same email or the same password already exists
	 */
	public static boolean registerUser(String username, String password, String email)
	{
		if (!usernameExists(username) && !emailExists(email))
		{
			byte[] salt = generateSalt();
			byte[] hashedPassword = saltPassword(password, salt);
			databaseConnector.registerUser(username, hashedPassword, email, salt);
			return true;
		}
		return false;
	}
	
	/**
	 * Overload of registerUser. Calls registerUser(String username, String password, String email)
	 *
	 * @param user BattleshipUser with information about the user
	 * @return boolean representing whether the registering of a new user was successful or not. Returns true if user was registered, and false if a user with either the same email or the same password already exists
	 */
	public static boolean registerUser(BattleshipUser user){
		return registerUser(user.getUsername(),user.getPassword(), user.getEmail());
	}
	
	/**
	 * Handles logging in with an existing user.
	 *
	 * @param username String containing the username of the user
	 * @param password String containing the password of the user
	 * @return BattleshipUser containing the information about the logged in user, or null if unsuccessful
	 */
	public static BattleshipUser login(String username, String password)
	{
		BattleshipUser user = databaseConnector.getBattleshipUser(username, password);
		if(user != null) Statics.setLocalUser(user);
		return user;
	}
	
	/**
	 * Checks if the passed username already exists in the database
	 *
	 * @param username String containing the username to check
	 * @return boolean true if username exists, false if not
	 */
	public static boolean usernameExists(String username)
	{
		return databaseConnector.stringExistsInColumn(username, USERS_USERNAME, USERS_TABLE);
	}
	
	/**
	 * Checks if the passed email address already exists in the database
	 *
	 * @param email String containing the email to check
	 * @return boolean true if email exists, false if not
	 */
	public static boolean emailExists(String email)
	{
		return databaseConnector.stringExistsInColumn(email, USERS_EMAIL, USERS_TABLE);
	}
	
	/**
	 * Hashes a password with the passed salt
	 *
	 * @param password String containing the password to hash
	 * @param salt     byte[] containing a salt for hashing the password
	 * @return byte[] representing the hashed password
	 * <p>
	 * //https://www.baeldung.com/java-password-hashing
	 * //https://docs.oracle.com/javase/6/docs/api/java/security/SecureRandom.html#getInstance(java.lang.String)
	 */
	public static byte[] saltPassword(String password, byte[] salt)
	{
		try
		{
			KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100, 128);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = factory.generateSecret(spec).getEncoded();
			return hash;
		}
		catch (Exception e)
		{
			Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
		}
		return null;
	}
	
	/**
	 * Generates a secure random salt for hashing passwords
	 *
	 * @return byte[] with secure random generated salt
	 */
	private static byte[] generateSalt()
	{
		SecureRandom secureRandom = new SecureRandom();
		byte[] salt = new byte[16];
		secureRandom.nextBytes(salt);
		return salt;
	}

	/**
	 * Logs the local user out of the database
	 *
	 * @return true if user was logged out successfully, false if not
	 */
	public static boolean logout() {
		return databaseConnector.logout();
	}

	/**
	 * @deprecated test method for Login class
	 * @param args standard main args
	 */
	public static void main(String[] args)
	{
		//try{Class.forName("com.mysql.cj.jdbc.Driver");}catch(Exception e){e.printStackTrace();}
		String username = "Tore";
		String password = "password";
		String email = "tore@mail.no";
		
		boolean registered = Login.registerUser(username, password, email);
		System.out.println(registered);
		BattleshipUser bp = Login.login("Tore", "password");
		System.out.println(bp.getUsername());
	}
}