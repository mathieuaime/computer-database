package com.excilys.computerdatabase.daos;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.stereotype.Component;

import com.excilys.computerdatabase.config.MainConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

public enum ConnectionMySQL {
    INSTANCE;

    private static ThreadLocal<Connection> connectionThread = new ThreadLocal<>();
    //private static HikariConfig config = new HikariConfig("/config/hikari.properties");
    //private static HikariDataSource dataSource = new HikariDataSource(config);

    @Autowired
    private static DataSource dataSource;

    /**
     * Open the connection.
     */
    public static void open() {
        /*try {
            connectionThread.set(dataSource.getConnection());
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }

    /**
     * Returns the connection and create it if its null.
     * @return Connection
     * @throws SQLException SQLException
     */
    public static Connection getConnection() throws SQLException {
        //return connectionThread.get();
        return dataSource.getConnection();
    }

    /**
     * Close the connection.
     */
    public static void close() {
        /*try {
            connectionThread.get().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }*/
    }
}
