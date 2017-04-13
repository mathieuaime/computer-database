package com.excilys.computerdatabase.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.excilys.computerdatabase.config.Config;

public enum ConnectionMySQL {

	INSTANCE;

	private static final String URL = Config.getProperties().getProperty("url");
	private static final String USER = Config.getProperties().getProperty("user");
	private static final String PASSWORD = Config.getProperties().getProperty("password");

	private static Connection con = null;

	public static void connect() {
		try {
			con = DriverManager.getConnection(URL, USER, PASSWORD);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void disconnect() {
		try {
			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	protected Connection getConnection() {
		try {
			if (con == null || con.isClosed()) {
				connect();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

}
