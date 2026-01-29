package com.library.util;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class for managing database connections using HikariCP connection
 * pooling.
 * Supports manual transaction management as required for the project.
 */
public class DatabaseConnection {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConnection.class);
    private static HikariDataSource dataSource;

    static {
        try (InputStream input = DatabaseConnection.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                logger.error("Unable to find application.properties");
                throw new RuntimeException("application.properties not found");
            }
            prop.load(input);

            HikariConfig config = new HikariConfig();
            config.setJdbcUrl(prop.getProperty("db.url"));
            config.setUsername(prop.getProperty("db.username"));
            config.setPassword(prop.getProperty("db.password"));
            config.setDriverClassName(prop.getProperty("db.driver"));

            // Pool configuration
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setIdleTimeout(30000);
            config.setConnectionTimeout(30000);
            config.setMaxLifetime(1800000);

            dataSource = new HikariDataSource(config);
            logger.info("Database connection pool initialized successfully");
        } catch (IOException e) {
            logger.error("Failed to load database configuration", e);
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }

    /**
     * Gets a connection from the pool.
     * 
     * @return A SQL Connection
     * @throws SQLException if connection cannot be established
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * Explicitly begins a transaction by setting auto-commit to false.
     * 
     * @param conn The connection to use
     * @throws SQLException if a database error occurs
     */
    public static void beginTransaction(Connection conn) throws SQLException {
        if (conn != null) {
            conn.setAutoCommit(false);
            logger.debug("Transaction started");
        }
    }

    /**
     * Commits the current transaction and restores auto-commit to true.
     * 
     * @param conn The connection to use
     * @throws SQLException if a database error occurs
     */
    public static void commit(Connection conn) throws SQLException {
        if (conn != null && !conn.getAutoCommit()) {
            conn.commit();
            conn.setAutoCommit(true);
            logger.debug("Transaction committed");
        }
    }

    /**
     * Rolls back the current transaction and restores auto-commit to true.
     * 
     * @param conn The connection to use
     * @throws SQLException if a database error occurs
     */
    public static void rollback(Connection conn) throws SQLException {
        if (conn != null && !conn.getAutoCommit()) {
            conn.rollback();
            conn.setAutoCommit(true);
            logger.debug("Transaction rolled back");
        }
    }

    /**
     * Closes the data source.
     */
    public static void closeDataSource() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("Database connection pool closed");
        }
    }
}
