package com.library.util;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Functional interface for database operations within a transaction that return
 * a result.
 */
@FunctionalInterface
public interface TransactionCallback<T> {
    T execute(Connection conn) throws SQLException;
}

/**
 * Functional interface for database operations within a transaction that do not
 * return a result.
 */
@FunctionalInterface
public interface TransactionAction {
    void execute(Connection conn) throws SQLException;
}
