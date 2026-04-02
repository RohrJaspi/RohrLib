package dev.rohrjaspi.rohrlib.database;

import java.sql.Connection;
import java.sql.SQLException;

public interface IDatabaseManager {

    /**
     * Connect to the database.
     * You can handle the creation of tables here,
     * or call other setup methods.
     */
    void connect();

    /**
     * Retrieve a Connection from the connection pool.
     * Useful for performing queries in different classes.
     */
    Connection getConnection() throws SQLException;

    /**
     * Close the pool or clean up resources.
     */
    void close();
}
