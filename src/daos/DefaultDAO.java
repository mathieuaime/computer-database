package daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import config.Config;

import java.sql.PreparedStatement;

public abstract class DefaultDAO {
	
	protected Connection con = null;
	protected PreparedStatement stmt = null;
    
	protected String URL = Config.URL;
	protected String LOGIN = Config.LOGIN;
	protected String PASSWORD = Config.PASSWORD;
	
	protected Connection getConnexion() throws SQLException {
    	
    	if (con == null || con.isClosed()) {
    		con = DriverManager.getConnection(URL, LOGIN, PASSWORD);
    	}
    	
        return con;
    }

}
