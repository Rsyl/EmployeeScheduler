package persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SqliteDB {
	Connection con = null;
	Statement stmt = null;
	
	SqliteDB(){
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:schedule.db");
			System.out.println("Connected to DB");
			con.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
}
