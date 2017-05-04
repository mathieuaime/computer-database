package com.excilys.computerdatabase.daos;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public enum ConnectionMySQL {

    INSTANCE;

    private static ThreadLocal<Connection> connectionThread = new ThreadLocal<>();
    private static HikariConfig config = new HikariConfig("/config/hikari.properties");
    private static HikariDataSource dataSource = new HikariDataSource(config);

    /**
     * Open the connection.
     */
    public static void open() {
        try {
            connectionThread.set(dataSource.getConnection());
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * Returns the connection and create it if its null.
     * @return Connection
     * @throws SQLException SQLException
     */
    public static Connection getConnection() throws SQLException {
        return connectionThread.get();
    }

    /**
     * Close the connection.
     */
    public static void close() {
        try {
            connectionThread.get().close();
        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
