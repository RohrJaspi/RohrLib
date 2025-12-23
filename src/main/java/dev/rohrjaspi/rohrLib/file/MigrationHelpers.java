package dev.rohrjaspi.rohrlib.file;

import org.bukkit.configuration.file.YamlConfiguration;

public final class MigrationHelpers {

    private MigrationHelpers() {}

    public static void renameKey(YamlConfiguration cfg, String oldPath, String newPath) {
        if (!cfg.contains(oldPath)) return;
        if (cfg.contains(newPath)) return; // don't overwrite user

        Object val = cfg.get(oldPath);
        cfg.set(newPath, val);
        cfg.set(oldPath, null);
    }

    public static void addIfMissing(YamlConfiguration cfg, String path, Object defaultValue) {
        if (!cfg.contains(path)) cfg.set(path, defaultValue);
    }

    public static void removeDeprecated(YamlConfiguration cfg, String path) {
        if (cfg.contains(path)) cfg.set(path, null);
    }
}