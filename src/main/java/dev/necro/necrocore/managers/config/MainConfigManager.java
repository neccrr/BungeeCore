package dev.necro.necrocore.managers.config;

import dev.necro.necrocore.NecroCore;
import dev.necro.necrocore.config.ConfigManager;
import dev.necro.necrocore.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.config.Configuration;

@Getter
@Setter
public class MainConfigManager {

    private final NecroCore plugin;

    private String prefix;
    private boolean silent;
    private boolean isUseConfirmation;

    public MainConfigManager(NecroCore plugin) {
        this.plugin = plugin;
        this.reload();
    }

    /**
     * Reloads the main configuration file
     */
    public void reload() {
        ConfigManager configManager = new ConfigManager(plugin);
        Configuration mainConfig = configManager.getConfig("config.yml");

        this.prefix = StringUtils.colorize(mainConfig.getString("prefix"));
        this.silent = mainConfig.getBoolean("use-silent-permission-check");
        this.isUseConfirmation = mainConfig.getBoolean("use-confirmation");
    }

    /**
     * Save the main configuration file
     */
    public void save() {
        ConfigManager configManager = new ConfigManager(plugin);
        Configuration mainConfig = configManager.getConfig("config.yml");

        mainConfig.set("prefix", this.prefix);
        mainConfig.set("use-silent-permission-check", this.silent);
        mainConfig.set("use-confirmation", this.isUseConfirmation);

        configManager.save("config.yml");
    }
}
