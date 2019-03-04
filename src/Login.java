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
            Scanner scanner = new Scanner(System.in);
            try(Connection con = DriverManager.getConnection(databaseUrl)){
                Statement stmt = con.createStatement();
                stmt.executeUpdate("INSERT INTO BattleshipUser(username,password,email) VALUES('"+username+"','"+ password+"','"+ email+"')");
                con.close();
            }
            catch(SQLException e){
                System.out.println(e);
            }
            return true;
        }
        return false;
    }
    
    public BattleshipUser login(String username, String password, String email) throws Exception{
        Scanner scanner = new Scanner(System.in);
        try(Connection con = DriverManager.getConnection(databaseUrl)){
            String query = "SELECT * FROM BattleshipUser WHERE username = ?";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        preparedStatement.setString(1, username);
            ResultSet res = preparedStatement.executeQuery();
            if (res.getString("password").equals(password)){
                return new BattleshipUser(username,password,email,res.getInt("won_games"),res.getInt("lost_games"));
            }
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }
    
    public static boolean usernameExists(String username) throws Exception{
        Scanner scanner = new Scanner(System.in);
        try(Connection con = DriverManager.getConnection(databaseUrl)){
	        String query = "SELECT username FROM BattleshipUser";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        ResultSet res = preparedStatement.executeQuery();
            
            while(res.next()){
                if(username == res.getString("username")){
                    return true;
                }
            }
            con.close();
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return false;
    }
    
    public static boolean emailExists(String email) throws Exception{
        Scanner scanner = new Scanner(System.in);
        try(Connection con = DriverManager.getConnection("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/thombje?user=thombje&password=TFWUfjmb")){
	        String query = "SELECT email FROM BattleshipUser";
	        PreparedStatement preparedStatement = con.prepareStatement(query);
	        ResultSet res = preparedStatement.executeQuery();
            
            while(res.next()){
                if(email.equals(res.getString("email"))){
                    return true;
                }
            }
            con.close();
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return false;
    }
    public static void main(String[] args){
        //try{Class.forName("com.mysql.cj.jdbc.Driver");}catch(Exception e){e.printStackTrace();}
        String username = "Tore";
        String password = "password";
        String email = "Torsk";
        try {
            boolean registered = Login.registerUser(username, password, email);
	        System.out.println(registered);
        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

