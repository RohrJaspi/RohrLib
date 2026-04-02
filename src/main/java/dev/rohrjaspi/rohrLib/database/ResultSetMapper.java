package dev.rohrjaspi.rohrlib.database;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Functional interface to map a single row from a ResultSet to a custom object.
 *
 * @param <T> The type of object to map to.
 */
@FunctionalInterface
public interface ResultSetMapper<T> {
    /**
     * Maps a single row from the ResultSet to a custom object.
     *
     * @param resultSet The ResultSet containing the row.
     * @return The mapped custom object.
     * @throws SQLException If an SQL error occurs during mapping.
     */
    T map(ResultSet resultSet) throws SQLException;
}
