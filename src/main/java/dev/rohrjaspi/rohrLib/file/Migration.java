package dev.rohrjaspi.rohrlib.file;

import org.bukkit.configuration.file.YamlConfiguration;

@FunctionalInterface
public interface Migration {
    void apply(YamlConfiguration user, YamlConfiguration defaults);
}