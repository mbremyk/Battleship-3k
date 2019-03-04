import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.*;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

//Gjør klassen abstract
public abstract class Login {
	
	private static String databaseUrl = "jdbc:mysql://mysql.stud.idi.ntnu.no:3306/thombje?user=thombje&password=TFWUfjmb";
 
	
	//Funksjoner er static
    public static boolean registerUser(String username, String password, String email) throws Exception{
        if(!usernameExists(username) && !emailExists(email)){
        	String salt = generateSalt();
        	//password = saltPassword(password, salt);
            try(Connection con = DriverManager.getConnection(databaseUrl)){
                String query = "INSERT INTO BattleshipUser(username,password,email,salt) VALUES(?,?,?,?)";
                PreparedStatement preparedStatement = con.prepareStatement(query);
                preparedStatement.setString(1, username);
	            preparedStatement.setString(2, password);
	            preparedStatement.setString(3, email);
	            preparedStatement.setString(4, salt);
                preparedStatement.execute();
                return true;
            }
            catch(SQLException ex){
	            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
                return false;
            }
        }
        return false;
    }
    
    public static BattleshipUser login(String username, String password, String email) throws Exception{
        Scanner scanner = new Scanner(System.in);
        try(Connection con = DriverManager.getConnection(databaseUrl)){
            String query = "SELECT * FROM BattleshipUser WHERE username = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, username);
            ResultSet res = preparedStatement.executeQuery();
            res.next();
            String passwordHash = res.getString("password");
            String salt = res.getString("salt");
            if (/*saltPassword(password, salt).equals(passwordHash)*/password.equals(res.getString("password"))){
                return new BattleshipUser(username,password,email,res.getInt("won_games"),res.getInt("lost_games"));
            }
        }
        catch(SQLException ex){
	        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public static boolean usernameExists(String username) throws Exception{
        try(Connection con = DriverManager.getConnection(databaseUrl)){
	        String query = "SELECT username FROM BattleshipUser";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
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
    
    public static boolean emailExists(String email) throws Exception{
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
        catch(SQLException ex){
	        Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }
    
	//https://www.baeldung.com/java-password-hashing
	//https://www.mkyong.com/java/how-do-convert-byte-array-to-string-in-java/
    private static String saltPassword(String password, String _salt) throws Exception
    {
	    byte[] salt = _salt.getBytes();
	    KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 100, 16);
	    SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
	    byte[] hash = factory.generateSecret(spec).getEncoded();
	    return new String(hash);
    }
    
    private static String generateSalt()
    {
	    SecureRandom secureRandom = new SecureRandom();
	    byte[] salt = new byte[16];
	    secureRandom.nextBytes(salt);
	    return new String(salt);
    }
    
    public static void main(String[] args){
        //try{Class.forName("com.mysql.cj.jdbc.Driver");}catch(Exception e){e.printStackTrace();}
        String username = "Magnus";
        String password = "password";
        String email = "magnus@mail.no";
        try {
            boolean registered = Login.registerUser(username, password, email);
	        System.out.println(registered);
	        BattleshipUser bp = Login.login("Magnus","password", "magnus@mail.no");
	        System.out.println(bp.getUsername());
        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

