package dev.necro.necrocore.config;

import dev.necro.necrocore.NecroCore;
import lombok.Getter;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

@Getter
public class ConfigManager {

    private final NecroCore plugin;

    private File configFile;

    private Configuration configuration;

    public ConfigManager(NecroCore plugin) {
        this.plugin = plugin;
    }

    public Configuration loadConfig(String filename) throws IOException {
        configFile = new File(plugin.getDataFolder(), filename);

        if (!plugin.getDataFolder().exists()) {
            plugin.getDataFolder().mkdirs();
        }

        if (!configFile.exists()) {
            InputStream in = plugin.getResourceAsStream(filename);
            Files.copy(in, configFile.toPath());
        }

        configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);

        return configuration;
    }

    public void reloadConfig(String filename) {
        configFile = new File(plugin.getDataFolder(), filename);

        try {
            configuration = ConfigurationProvider.getProvider(YamlConfiguration.class).load(configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Cannot reload the Config! There might be a problem with the config file.");
            e.printStackTrace();
        }
    }

    public void saveConfig(String filename) {
        configFile = new File(plugin.getDataFolder(), filename);

        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(configuration, configFile);
        } catch (IOException e) {
            plugin.getLogger().severe("Cannot save the Config! There might be a problem with the config file.");
            e.printStackTrace();
        }
    }
}
