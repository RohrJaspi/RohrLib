package dev.rohrjaspi.rohrlib.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.Objects;
import java.util.logging.Logger;

public final class FileLoader {

    private static final String VERSION_KEY = "config-version";

    private final Path folder;
    private final String fileName;
    private final ResourceProvider resourceProvider;
    private final Logger logger;
    private final MigrationRegistry migrations; // optional

    private File configFile;
    private YamlConfiguration user;
    private YamlConfiguration defaults;

    /**
     * @param migrations can be null if you only want auto-add new keys
     */
    public FileLoader(Path folder,
                      String fileName,
                      ResourceProvider resourceProvider,
                      Logger logger,
                      MigrationRegistry migrations) {
        this.folder = Objects.requireNonNull(folder);
        this.fileName = Objects.requireNonNull(fileName);
        this.resourceProvider = Objects.requireNonNull(resourceProvider);
        this.logger = Objects.requireNonNull(logger);
        this.migrations = migrations; // may be null
    }

    /** Convenience: no migrations */
    public FileLoader(Path folder,
                      String fileName,
                      ResourceProvider resourceProvider,
                      Logger logger) {
        this(folder, fileName, resourceProvider, logger, null);
    }

    public FileConfiguration loadAndUpdate() {
        this.configFile = folder.resolve(fileName).toFile();
        ensureFileExistsFromResource();

        this.user = YamlConfiguration.loadConfiguration(configFile);
        this.defaults = loadDefaults();

        if (this.defaults == null) {
            logger.warning("Defaults not found for '" + fileName + "' (resource missing). Update skipped.");
            return user;
        }

        // Optional: allows cfg.getX to fallback to defaults when not set
        this.user.setDefaults(this.defaults);

        // Determine versions from file + defaults
        int latest = this.defaults.getInt(VERSION_KEY, 1);
        int current = this.user.getInt(VERSION_KEY, 1);

        // 1) migrations (only if registry provided)
        while (current < latest) {
            if (migrations != null) {
                Migration step = migrations.getStep(current);
                if (step != null) step.apply(user, defaults);
            }
            current++;
            user.set(VERSION_KEY, current);
        }

        // 2) add-only merge (never overwrite)
        mergeMissingKeys(user, defaults);

        // 3) ensure version key exists
        if (!user.contains(VERSION_KEY)) user.set(VERSION_KEY, latest);

        save();
        return user;
    }

    private void mergeMissingKeys(FileConfiguration user, FileConfiguration def) {
        for (String key : def.getKeys(true)) {
            if (!user.contains(key)) {
                user.set(key, def.get(key));
            }
        }
    }

    private void ensureFileExistsFromResource() {
        File parent = configFile.getParentFile();
        if (parent != null && !parent.exists()) {
            //noinspection ResultOfMethodCallIgnored
            parent.mkdirs();
        }

        if (!configFile.exists()) {
            try (InputStream in = resourceProvider.open(fileName)) {
                if (in == null) {
                    //noinspection ResultOfMethodCallIgnored
                    configFile.createNewFile();
                    return;
                }
                try (OutputStream out = new FileOutputStream(configFile)) {
                    byte[] buffer = new byte[8192];
                    int read;
                    while ((read = in.read(buffer)) != -1) out.write(buffer, 0, read);
                }
            } catch (IOException e) {
                logger.severe("Could not create config '" + fileName + "': " + e.getMessage());
            }
        }
    }

    private YamlConfiguration loadDefaults() {
        try (InputStream in = resourceProvider.open(fileName)) {
            if (in == null) return null;
            try (Reader r = new InputStreamReader(in, StandardCharsets.UTF_8)) {
                return YamlConfiguration.loadConfiguration(r);
            }
        } catch (IOException e) {
            logger.severe("Could not load defaults for '" + fileName + "': " + e.getMessage());
            return null;
        }
    }

    private void save() {
        try {
            user.save(configFile);
        } catch (IOException e) {
            logger.severe("Could not save config '" + fileName + "': " + e.getMessage());
        }
    }
}