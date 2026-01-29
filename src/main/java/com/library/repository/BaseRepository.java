package com.library.repository;

import com.library.util.DatabaseConnection;
import com.library.util.TransactionAction;
import com.library.util.TransactionCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Base repository providing transaction infrastructure for all repositories.
 * Implementation of the GOF Template Method pattern for transaction management.
 */
public abstract class BaseRepository {
    private static final Logger logger = LoggerFactory.getLogger(BaseRepository.class);

    /**
     * Executes a set of database operations within a single transaction and returns
     * a result.
     * Automatically handles begin, commit, and rollback.
     * 
     * @param callback The operations to perform
     * @param <T>      The return type
     * @return The result of the operations
     * @throws RuntimeException if a database error occurs (wraps SQLException)
     */
    protected <T> T executeInTransaction(TransactionCallback<T> callback) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            DatabaseConnection.beginTransaction(conn);

            T result = callback.execute(conn);

            DatabaseConnection.commit(conn);
            return result;
        } catch (SQLException e) {
            logger.error("Transaction failed, rolling back", e);
            try {
                DatabaseConnection.rollback(conn);
            } catch (SQLException ex) {
                logger.error("Rollback failed", ex);
            }
            throw new RuntimeException("Database transaction failed", e);
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * Executes a set of database operations within a single transaction without
     * returning a result.
     * Automatically handles begin, commit, and rollback.
     * 
     * @param action The operations to perform
     * @throws RuntimeException if a database error occurs (wraps SQLException)
     */
    protected void executeInTransaction(TransactionAction action) {
        Connection conn = null;
        try {
            conn = DatabaseConnection.getConnection();
            DatabaseConnection.beginTransaction(conn);

            action.execute(conn);

            DatabaseConnection.commit(conn);
        } catch (SQLException e) {
            logger.error("Transaction failed, rolling back", e);
            try {
                DatabaseConnection.rollback(conn);
            } catch (SQLException ex) {
                logger.error("Rollback failed", ex);
            }
            throw new RuntimeException("Database transaction failed", e);
        } finally {
            closeConnection(conn);
        }
    }

    /**
     * Safely closes a database connection.
     * 
     * @param conn The connection to close
     */
    protected void closeConnection(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                logger.error("Failed to close connection", e);
            }
        }
    }
}
