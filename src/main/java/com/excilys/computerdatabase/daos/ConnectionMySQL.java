package com.excilys.computerdatabase.daos;

import java.sql.Connection;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public enum ConnectionMySQL {

    INSTANCE;

    private static HikariDataSource dataSource = null;

    /**
     * Open the connection.
     */
    public static void connect() {
        HikariConfig config = new HikariConfig("/config/hikari.properties");
        dataSource = new HikariDataSource(config);
    }

    /**
     * Returns the connection and create it if its null.
     * @return Connection
     * @throws SQLException SQLException
     */
    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            connect();
        }

        return dataSource.getConnection();
    }

}
