package dev.rohrjaspi.rohrLib.utils;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

public class ConfigLoader {

	private FileConfiguration config;
	private File configFile;
	private final String fileName;
	private final Path path;
	private final JavaPlugin plugin;

	public ConfigLoader(JavaPlugin plugin, Path path, String fileName) {
		this.plugin = plugin;
		this.fileName = fileName;
		this.path = path;
	}

	public void loadFile() {
		this.configFile = new File(this.path.toFile(), this.fileName);
		if (!this.configFile.exists()) {
			this.plugin.saveResource(this.fileName, false);
		}

		this.config = YamlConfiguration.loadConfiguration(this.configFile);
		InputStream defaultStream = this.plugin.getResource(this.fileName);
		if (defaultStream != null) {
			YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
			this.config.setDefaults(defaultConfig);
		}

	}

	public FileConfiguration getFile() {
		if (this.config == null) {
			this.loadFile();
		}

		return this.config;
	}

	public void saveFile() {
		try {
			if (this.config != null && this.configFile != null) {
				this.config.save(this.configFile);
			}
		} catch (IOException var2) {
			Bukkit.getLogger().severe("Could not save config file: " + this.fileName);
		}

	}

	public void reloadConfig() {
		if (this.config == null) {
			this.configFile = new File(this.path.toFile(), this.fileName);
		}

		// Load the main configuration file
		this.config = YamlConfiguration.loadConfiguration(this.configFile);

		// Load default configuration from the plugin jar
		try (InputStream defaultConfigStream = this.plugin.getResource(this.fileName)) {
			if (defaultConfigStream != null) {
				Reader reader = new InputStreamReader(defaultConfigStream, StandardCharsets.UTF_8);
				YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(reader);
				this.config.setDefaults(defaultConfig);
			} else {
				this.plugin.getLogger().warning("Default config resource '" + this.fileName + "' not found in the plugin jar.");
			}
		} catch (IOException e) {
			this.plugin.getLogger().severe("Error loading default configuration: " + e.getMessage());
			e.printStackTrace();
		}
	}

	public void set(String path, Object value) {

		if (this.config == null) {
			this.loadFile();
		}

		if (path == null) {
			throw new IllegalArgumentException("Path cannot be null");
		}

		this.getFile().set(path, value);
		this.saveFile();
	}



}
