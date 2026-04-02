package dev.rohrjaspi.rohrlib.database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.plugin.java.JavaPlugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;

public abstract class HikariDatabaseManager implements IDatabaseManager {


    protected final JavaPlugin plugin;
    private final ExecutorService executorService;

    private HikariDataSource dataSource;
    private AsyncDatabaseOperations asyncOperations;

    protected HikariDatabaseManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.executorService = Executors.newFixedThreadPool(4);
    }


    protected abstract String getJdbcUrl();

    protected abstract String getUsername();

    protected abstract String getPassword();

    /**
     * Override this if you want custom Hikari settings.
     */
    protected void applyCustomSettings(HikariConfig config) {
        // Optional for subclasses
    }

    /**
     * Connect to the database and initialize the HikariDataSource.
     * This is where you configure HikariCP and create the pool.
     */
    @Override
    public void connect() {
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(getJdbcUrl());
        config.setUsername(getUsername());
        config.setPassword(getPassword());

        // Default pool settings
        config.setMaximumPoolSize(10);
        config.setMinimumIdle(2);
        config.setIdleTimeout(60000);
        config.setMaxLifetime(1800000);
        config.setConnectionTimeout(30000);

        applyCustomSettings(config);

        this.dataSource = new HikariDataSource(config);
        this.asyncOperations = new AsyncDatabaseOperations(plugin, this, executorService);
    }


    @Override
    public Connection getConnection() throws SQLException {
        if (dataSource == null) {
            throw new SQLException("DataSource is not initialized. Call connect() first.");
        }
        return dataSource.getConnection();
    }

    public AsyncDatabaseOperations async() {
        if (asyncOperations == null) {
            throw new IllegalStateException("Database is not connected yet. Call connect() first.");
        }
        return asyncOperations;
    }

    /**
     * Helper method for table creation or sync startup statements.
     */
    public void executeSync(String sql) {
        try (Connection connection = getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
        } catch (SQLException exception) {
            plugin.getLogger().log(Level.SEVERE, "Failed to execute sync SQL: " + sql, exception);
        }
    }

    @Override
    public void close() {
        if (dataSource != null) {
            dataSource.close();
        }

        executorService.shutdown();
    }
}
