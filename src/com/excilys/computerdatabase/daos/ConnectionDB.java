package com.excilys.computerdatabase.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.excilys.computerdatabase.config.Config;

import java.sql.PreparedStatement;

public abstract class ConnectionDB {

	protected static Connection con = null;

	protected static final String URL = Config.URL;
	protected static final String LOGIN = Config.LOGIN;
	protected static final String PASSWORD = Config.PASSWORD;

	protected static Connection getConnexion() throws SQLException {

		if (con == null || con.isClosed()) {
			con = DriverManager.getConnection(URL, LOGIN, PASSWORD);
		}

		return con;
	}

}
