package dev.rohrjaspi.rohrlib.database;

import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.logging.Level;

public class AsyncDatabaseOperations {

    private final JavaPlugin plugin;
    private final HikariDatabaseManager databaseManager;
    private final Executor executor;

    public AsyncDatabaseOperations(JavaPlugin plugin, HikariDatabaseManager databaseManager, Executor executor) {
        this.plugin = plugin;
        this.databaseManager = databaseManager;
        this.executor = executor;
    }

    public CompletableFuture<Integer> updateAsync(String sql, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = databaseManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                setParameters(statement, params);
                return statement.executeUpdate();

            } catch (SQLException exception) {
                plugin.getLogger().log(Level.SEVERE, "Failed to execute async update: " + sql, exception);
                throw new RuntimeException(exception);
            }
        }, executor);
    }

    public <T> CompletableFuture<T> queryOneAsync(String sql, ResultSetMapper<T> mapper, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            try (Connection connection = databaseManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                setParameters(statement, params);

                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return mapper.map(resultSet);
                    }
                    return null;
                }

            } catch (SQLException exception) {
                plugin.getLogger().log(Level.SEVERE, "Failed to execute async queryOne: " + sql, exception);
                throw new RuntimeException(exception);
            }
        }, executor);
    }

    public <T> CompletableFuture<List<T>> queryListAsync(String sql, ResultSetMapper<T> mapper, Object... params) {
        return CompletableFuture.supplyAsync(() -> {
            List<T> results = new ArrayList<>();

            try (Connection connection = databaseManager.getConnection();
                 PreparedStatement statement = connection.prepareStatement(sql)) {

                setParameters(statement, params);

                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        results.add(mapper.map(resultSet));
                    }
                }

            } catch (SQLException exception) {
                plugin.getLogger().log(Level.SEVERE, "Failed to execute async queryList: " + sql, exception);
                throw new RuntimeException(exception);
            }

            return results;
        }, executor);
    }

    private void setParameters(PreparedStatement statement, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            statement.setObject(i + 1, params[i]);
        }
    }
}