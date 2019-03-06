/**
 * Login.java
 *
 * Handles registering of new users, login of existing users and salting and hashing passwords
 *
 * @Author Brevik Magnus
 * @Author Bjerke Thomas
 * @Author Gulaker Kristian
 * @Author Thorkildsen Torje
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
            }
            catch(SQLException e){
	            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
                return false;
            }
            catch(Exception e)
            {
            	e.printStackTrace();
            }
        }
        return false;
    }
	
	/**
	 *
	 * @param username
	 * @param password
	 * @param email
	 * @return
	 * @throws Exception
	 */
	public static BattleshipUser login(String username, String password, String email) {
        try(Connection con = DriverManager.getConnection(databaseUrl)){
            String query = "SELECT * FROM BattleshipUser WHERE username = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, username);
            ResultSet res = preparedStatement.executeQuery();
            if(res.next())
            {
	            byte[] passwordHash = res.getBytes("password");
	            byte[] salt = res.getBytes("salt");
	            if (Arrays.equals(saltPassword(password, salt), passwordHash)/*password.equals(res.getString("password")*/)
	            {
		            return new BattleshipUser(username, password, email, res.getInt("won_games"), res.getInt("lost_games"));
	            }
            }
        }
        catch(SQLException e){
	        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }
        catch (Exception e)
        {
	        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }
	
	/**
	 *
	 * @param username
	 * @return
	 */
	public static boolean usernameExists(String username) {
        try(Connection con = DriverManager.getConnection(databaseUrl)){
	        String query = "SELECT username FROM BattleshipUser";// WHERE username = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        //preparedStatement.setString(1, username);
	        ResultSet res = preparedStatement.executeQuery();
            
            while(res.next()){
                if(username == res.getString("username")){
                    return true;
                }
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return false;
    }
	
	/**
	 *
	 * @param email
	 * @return
	 * @throws Exception
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
        }
        catch(SQLException e){
	        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }
	
	/**
	 *
	 * @param password
	 * @param salt
	 * @return
	 * @throws Exception
	 *
	 * //https://www.baeldung.com/java-password-hashing
	 * //https://www.mkyong.com/java/how-do-convert-byte-array-to-string-in-java/
	 */
    private static byte[] saltPassword(String password, byte[] salt) throws Exception
    {
	    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100, 16);
	    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	    byte[] hash = factory.generateSecret(spec).getEncoded();
	    return hash;
    }
	
	/**
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
	
	/**
	 *
	 * @param args
	 */
	public static void main(String[] args){
        //try{Class.forName("com.mysql.cj.jdbc.Driver");}catch(Exception e){e.printStackTrace();}
        String username = "Tore";
        String password = "password";
        String email = "tore@mail.no";
        try {
            boolean registered = Login.registerUser(username, password, email);
	        System.out.println(registered);
	        BattleshipUser bp = Login.login("Tore","password", "tore@mail.no");
	        System.out.println(bp.getUsername());
        } catch (Exception e) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}