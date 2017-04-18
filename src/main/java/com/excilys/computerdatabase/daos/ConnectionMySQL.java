package com.excilys.computerdatabase.daos;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.slf4j.Logger;

import com.excilys.computerdatabase.config.Config;

public enum ConnectionMySQL {

    INSTANCE;

    private static final String URL = Config.getProperties().getProperty("url");
    private static final String USER = Config.getProperties().getProperty("user");
    private static final String PASSWORD = Config.getProperties().getProperty("password");

    private static Connection con = null;

    private static final Logger LOGGER = org.slf4j.LoggerFactory.getLogger(ConnectionMySQL.class);

    /**
     * Open the connection.
     */
    public static void connect() {
        try {
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    /**
     * Close the connection.
     */
    public static void disconnect() {
        try {
            con.close();
        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
    }

    /**
     * Returns the connection and create it if its null.
     * @return Connection
     */
    protected Connection getConnection() {
        try {
            if (con == null || con.isClosed()) {
                connect();
            }
        } catch (SQLException e) {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug("Exception: " + e);
            }
        }
        return con;
    }

}
