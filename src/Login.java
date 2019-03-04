import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Login {
    private String username;
    private String password;
    private String email;
    
    public Login(String username, String password, String email){
        this.username = username;
        this.password = password;
        this.email = email;
    }
    public boolean registerUser() throws Exception{
        if(!usernameExists() && !emailExists()){
            Scanner scanner = new Scanner(System.in);
            try(Connection con = DriverManager.getConnection("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/thombje?user=thombje&password=TFWUfjmb")){
                Statement stmt = con.createStatement();
                stmt.executeUpdate("INSERT INTO BattleshipUser(username,password,email) VALUES('"+this.username+"','"+ this.password+"','"+ this.email+"')");
                con.close();
            }
            catch(SQLException e){
                System.out.println(e);
            }
            return true;
        }
        return false;
    }
    
    public BattleshipUser login() throws Exception{
        Scanner scanner = new Scanner(System.in);
        try(Connection con = DriverManager.getConnection("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/thombje?user=thombje&password=TFWUfjmb")){
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT * FROM BattleshipUser WHERE username ="+this.username);
            if (res.getString("password").equals(this.password)){
                return new BattleshipUser(this.username,this.password,this.email,res.getInt("won_games"),res.getInt("lost_games"));
            }
            con.close();
        }
        catch(SQLException e){
            System.out.println(e);
        }
        return null;
    }
    
    public boolean usernameExists() throws Exception{
        Scanner scanner = new Scanner(System.in);
        try(Connection con = DriverManager.getConnection("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/thombje?user=thombje&password=TFWUfjmb")){
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT username FROM BattleshipUser");
            
            while(res.next()){
                if(this.username == res.getString("username")){
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
    public boolean emailExists() throws Exception{
        Scanner scanner = new Scanner(System.in);
        try(Connection con = DriverManager.getConnection("jdbc:mysql://mysql.stud.idi.ntnu.no:3306/thombje?user=thombje&password=TFWUfjmb")){
            Statement stmt = con.createStatement();
            ResultSet res = stmt.executeQuery("SELECT email FROM BattleshipUser");
            
            while(res.next()){
                if(this.email.equals(res.getString("email"))){
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
        Login newLogin = new Login("kristian","password","krissi");
        try {
            boolean registered = newLogin.registerUser();
        } catch (Exception ex) {
            Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

