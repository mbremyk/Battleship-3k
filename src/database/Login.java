package database;

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

import model.BattleshipUser;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Arrays;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

import static database.Constants.*;


public abstract class Login {

    private static String databaseUrl = "jdbc:mysql://" + DB_HOST + ":" + DB_PORT + "/" + DB_NAME + "?user=" + DB_USER + "&password=" + DB_PASSWORD;

    //Funksjoner er static
    public static boolean registerUser(String username, String password, String email) throws Exception {
        if (!usernameExists(username) && !emailExists(email)) {
            byte[] salt = generateSalt();
            byte[] hashedPassword = saltPassword(password, salt);
            try (Connection con = DriverManager.getConnection(databaseUrl)) {
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

    public static BattleshipUser login(String username, String password, String email) throws Exception {
        Scanner scanner = new Scanner(System.in);
        try (Connection con = DriverManager.getConnection(databaseUrl)) {
            String query = "SELECT * FROM BattleshipUser WHERE username = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet res = preparedStatement.executeQuery();
            if (res.next()) {
                byte[] passwordHash = res.getBytes("password");
                byte[] salt = res.getBytes("salt");
                if (Arrays.equals(saltPassword(password, salt), passwordHash)/*password.equals(res.getString("password")*/) {
                    return new BattleshipUser(username, password, email, res.getInt("won_games"), res.getInt("lost_games"));
                }
            }
        } catch (SQLException e) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }
        return null;
    }

    public static boolean usernameExists(String username) throws Exception {
        try (Connection con = DriverManager.getConnection(databaseUrl)) {
            String query = "SELECT username FROM BattleshipUser WHERE username = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, username);
            ResultSet res = preparedStatement.executeQuery();

            if (res.next()) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e);
        }
        return false;
    }

    public static boolean emailExists(String email) throws Exception {
        try (Connection con = DriverManager.getConnection(databaseUrl)) {
            String query = "SELECT email FROM BattleshipUser WHERE email = ?";
            PreparedStatement preparedStatement = con.prepareStatement(query);
            preparedStatement.setString(1, email);
            ResultSet res = preparedStatement.executeQuery();

            if (res.next()) {
                return true;
            }
        } catch (SQLException e) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }
        return false;
    }

    //https://www.baeldung.com/java-password-hashing
    //https://www.mkyong.com/java/how-do-convert-byte-array-to-string-in-java/
    private static byte[] saltPassword(String password, byte[] salt) throws Exception {
        KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100, 16);
        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] hash = factory.generateSecret(spec).getEncoded();
        return hash;
    }

    private static byte[] generateSalt() {
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
            BattleshipUser bp = Login.login("Tore", "password", "tore@mail.no");
            System.out.println(bp.getUsername());
        } catch (Exception e) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, e);
        }
    }
}