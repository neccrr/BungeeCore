package dev.necro.necrocore.config;

import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.ConfigurationProvider;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

public class ConfigManager {
    Configuration config;
    Configuration templateConfig;
    List<String> configKeys = new ArrayList<>();
    List<String> templateKeys = new ArrayList<>();
    Logger log;
    Plugin plugin;

    public ConfigManager(Plugin plugin) {
        this.plugin = plugin;
        log = plugin.getLogger();
    }

    /**
     * Get a configuration file
     *
     * @param filename The name of the file
     */
    public Configuration getConfig(String filename) {
        if (!plugin.getDataFolder().exists())
            plugin.getDataFolder().mkdir();

        File file = new File(plugin.getDataFolder(), filename);

        if (!file.exists()) {
            try (InputStream in = plugin.getResourceAsStream(filename)) {
                Files.copy(in, file.toPath());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            config = ConfigurationProvider.getProvider(YamlConfiguration.class).load(new File(plugin.getDataFolder(), filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Get all keys from the real file
        for (String key : config.getKeys()) {
            configKeys.add(key);

            if (config.get(key) instanceof Configuration) {
                iterateKey(key, configKeys, config);
            }
        }

        // Get template config file
        templateConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(plugin.getResourceAsStream(filename));

        // Get all keys from the template
        for (String key : templateConfig.getKeys()) {
            templateKeys.add(key);

            if (templateConfig.get(key) instanceof Configuration) {
                iterateKey(key, templateKeys, templateConfig);
            }
        }

        Collections.reverse(configKeys);
        Collections.reverse(templateKeys);

        for (String key : templateKeys) {
            if (!config.contains(key)) {
                config.set(key, templateConfig.get(key));
            }
        }

        for (String key : configKeys) {
            if (!templateConfig.contains(key)) {
                config.set(key, null);
            } else if (!templateConfig.get(key).getClass().equals(config.get(key).getClass())) {
                config.set(key, templateConfig.get(key));
            }

            templateKeys = new ArrayList<>();

            // Get template config file
            templateConfig = ConfigurationProvider.getProvider(YamlConfiguration.class).load(plugin.getResourceAsStream(filename));

            for (String tkey : templateConfig.getKeys()) {
                templateKeys.add(tkey);

                if (templateConfig.get(tkey) instanceof Configuration) {
                    iterateKey(tkey, templateKeys, templateConfig);
                }
            }

            Collections.reverse(templateKeys);
        }

        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(plugin.getDataFolder(), filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return config;
    }

    void iterateKey(String gkey, List<String> keylist, Configuration config) {
        for (String key : config.getSection(gkey).getKeys()) {
            keylist.add(gkey + "." + key);

            if (config.get(gkey + "." + key) instanceof Configuration) {
                iterateKey(gkey + "." + key, keylist, config);
            }
        }
    }

    /**
     * Save a configuration file
     *
     * @param filename The name of the file
     */
    public void save(String filename) {
        try {
            ConfigurationProvider.getProvider(YamlConfiguration.class).save(config, new File(plugin.getDataFolder(), filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}