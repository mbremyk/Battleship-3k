/**
 * @author NTNU
 * @deprecated not in use, just used as an example
 * <p>
 * JDBCExample.java
 */

package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class JDBCExample
{
	public static void main(String[] args) {
		System.out.println("Starter opp...");

		String url = "jdbc:mysql://mysql.stud.idi.ntnu.no:3306/thombje?user=thombje&password=TFWUfjmb";

		try (Connection con = DriverManager.getConnection(url);) {
			Statement statement = con.createStatement();
			//ResultSet res = "select * from person order by fornavn"
			statement.execute("alter table t set thing = 1 where thing = 1");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
