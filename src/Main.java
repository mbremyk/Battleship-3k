import database.DatabaseConnector;

import static database.Constants.*;

public class Main
{
	public static void main(String[] args)
	{
		DatabaseConnector db = new DatabaseConnector(DB_URL);
		System.out.println("Hello World!");
	}
}
