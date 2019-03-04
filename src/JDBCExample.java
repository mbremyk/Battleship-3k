import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class JDBCEksempel
{
	public static void main(String[] args)
	{
		System.out.println("Starter opp...");
		
		String url = "jdbc:mysql://mysql.stud.iie.ntnu.no:3306/magbre?user=magbre&password=cH96AClM";
		
		try (Connection con = DriverManager.getConnection(url);)
		{
			Statement statement = con.createStatement();
			//ResultSet res = "select * from person order by fornavn"
			statement.execute("insert into ansatt values (1, 'Magnus')");
			statement.execute("insert into ansatt values (2, 'Åse')");
			statement.execute("insert into ansatt values (3, 'Anne')");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
