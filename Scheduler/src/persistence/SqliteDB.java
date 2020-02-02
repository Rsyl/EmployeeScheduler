package persistence;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class SqliteDB {
	Connection con = null;
	Statement stmt = null;
	
	public SqliteDB(){
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:schedule.db");
			System.out.println("Connected to DB");
			createEmployeeTable();
			con.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	//Creates an employeeTable if table doesn't already exists
	private void createEmployeeTable() {
		try {
			Class.forName("org.sqlite.JDBC");
			con = DriverManager.getConnection("jdbc:sqlite:schedule.db");
			stmt = con.createStatement();
			String sql = "CREATE TABLE IF NOT EXISTS Employee "
					+ "( eID INTEGER PRIMARY KEY AUTOINCREMENT, "
					+ "eName TEXT NOT NULL)";
			stmt.executeUpdate(sql);
			System.out.println("Created Employee table");
			stmt.close();
			con.close();
		} catch (Exception e) {
			System.out.println("Error: " + e.getMessage());
		}
	}
	
	
	
}
