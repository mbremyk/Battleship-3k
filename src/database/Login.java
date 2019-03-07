/**
 * database.Login.java
 * <p>
 * Handles registering of new users, login of existing users and salting and hashing passwords
 *
 * @Author Brevik Magnus
 * @Author Bjerke Thomas
 * @Author Gulaker Kristian
 * @Author Thorkildsen Torje
 *
 * TODO Use constants from Constants.java instead of the hardcoded strings
 */

package database;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.*;

import static database.Constants.*;

/**
 * Login class
 */
public abstract class Login {
	
	private static String databaseUrl = "jdbc:mysql://mysql.stud.idi.ntnu.no:3306/thombje?user=thombje&password=TFWUfjmb";
	
	/**
	 * Handles registering of a new user with the desired username, password, and email.
	 * Hashes password using the generateSalt() and saltPassword() functions
	 *
	 * @param username String containing the desired username
	 * @param password String containing the desired password
	 * @param email String containing the desired email address
	 * @return boolean representing whether the registering of a new user was successful or not. Returns true if user was registered, and false if a user with either the same email or the same password already exists
	 */
    public static boolean registerUser(String username, String password, String email){
        if(!usernameExists(username) && !emailExists(email)){
            try(Connection con = DriverManager.getConnection(databaseUrl)){
	            byte[] salt = generateSalt();
	            byte[] hashedPassword = saltPassword(password, salt);
                String query = "INSERT INTO BattleshipUser(username,password,email,salt) VALUES(?,?,?,?)";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, username);
                preparedStatement.setBytes(2, hashedPassword);
                preparedStatement.setString(3, email);
                preparedStatement.setBytes(4, salt);
                preparedStatement.execute();
                return true;
            } catch (SQLException e) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }
	
	/**
	 * Handles logging in with an existing user.
	 *
	 * @param username String containing the username of the user
	 * @param password String containing the password of the user
	 * @return BattleshipUser containing the information about the logged in user, or null if unsuccessful
	 */
	public static BattleshipUser login(String username, String password) {
        try(Connection con = DriverManager.getConnection(databaseUrl)){
            String query = "SELECT * FROM BattleshipUser WHERE username = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                byte[] passwordHash = res.getBytes("password");
                byte[] salt = res.getBytes("salt");
                if (Arrays.equals(saltPassword(password, salt), passwordHash)/*password.equals(res.getString("password")*/) {
                    return new BattleshipUser(username, password, res.getString("email"), res.getInt("won_games"), res.getInt("lost_games"));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (Exception e)
        {
	        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
	
	/**
	 * Checks if the passed username already exists in the database
	 *
	 * @param username String containing the username to check
	 * @return boolean true if username exists, false if not
	 */
	public static boolean usernameExists(String username) {
        try(Connection con = DriverManager.getConnection(databaseUrl)){
	        String query = "SELECT username FROM BattleshipUser WHERE username = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, username);
	        ResultSet res = preparedStatement.executeQuery();
            
            while(res.next()){
                if(username == res.getString("username")){
                    return true;
                }
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        catch(Exception e)
        {
        
        }
        return false;
    }
	
	/**
	 * Checks if the passed email address already exists in the database
	 *
	 * @param email String containing the email to check
	 * @return boolean true if email exists, false if not
	 */
	public static boolean emailExists(String email) {
        try(Connection con = DriverManager.getConnection(databaseUrl)){
	        String query = "SELECT email FROM BattleshipUser";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        ResultSet res = preparedStatement.executeQuery();
            
            while(res.next()){
                if(email.equals(res.getString("email"))){
                    return true;
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }
	
	/**
	 * Hashes a password with the passed salt
	 *
	 * @param password String containing the password to hash
	 * @param salt byte[] containing a salt for hashing the password
	 * @return byte[] representing the hashed password
	 *
	 * //https://www.baeldung.com/java-password-hashing
	 * //https://www.mkyong.com/java/how-do-convert-byte-array-to-string-in-java/
	 */
    private static byte[] saltPassword(String password, byte[] salt) throws Exception
    {
	    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100, 128);
	    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	    byte[] hash = factory.generateSecret(spec).getEncoded();
	    return hash;
    }
	
	/**
	 * Generates a secure random salt for hashing passwords
	 *
	 * @return
	 */
	private static byte[] generateSalt()
    {
	    SecureRandom secureRandom = new SecureRandom();
	    byte[] salt = new byte[16];
	    secureRandom.nextBytes(salt);
	    return salt;
    }

    public static void main(String[] args) {
        //try{Class.forName("com.mysql.cj.jdbc.Driver");}catch(Exception e){e.printStackTrace();}
        String username = "Tore";
        String password = "password";
        String email = "tore@mail.no";
        try {
            boolean registered = Login.registerUser(username, password, email);
            System.out.println(registered);
            BattleshipUser bp = Login.login("Tore", "password");
            System.out.println(bp.getUsername());
        } catch (Exception e) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}