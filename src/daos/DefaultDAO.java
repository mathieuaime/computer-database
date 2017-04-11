package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;

public abstract class DefaultDAO {
	
	protected Connection con = null;
	protected PreparedStatement stmt = null;
    
	protected String URL = "jdbc:mysql://localhost:3306/computer-database-db?zeroDateTimeBehavior=convertToNull";
	protected String LOGIN = "root";
	protected String PASSWORD = "root";
	
	protected Connection getConnexion() throws SQLException {
    	
    	if (con == null || con.isClosed()) {
    		con = DriverManager.getConnection(URL, LOGIN, PASSWORD);
    	}
    	
        return con;
    }

}
