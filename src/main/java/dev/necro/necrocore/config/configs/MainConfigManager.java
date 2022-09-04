package dev.necro.necrocore.config.configs;

import dev.necro.necrocore.NecroCore;
import dev.necro.necrocore.config.ConfigManager;
import dev.necro.necrocore.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.config.Configuration;

import java.util.List;

@Getter
@Setter
public class MainConfigManager {

    private final NecroCore plugin;
    private final ConfigManager configManager;

    private String PREFIX;
    private boolean SILENT_PERMISSION_CHECK;
    private boolean USE_COMMAND_CONFIRMATION;

    private long GLOBALCHAT_COOLDOWN;
    private boolean GLOBALCHAT_LOGS_TO_CONSOLE;

    private List<String> HUB_SERVERS;

    public MainConfigManager(NecroCore plugin) {
        this.plugin = plugin;
        this.configManager = new ConfigManager(plugin);
        this.reload();
    }

    /**
     * Reloads the main configuration file
     */
    public void reload() {
        Configuration mainConfig = configManager.getConfig("config.yml");

        // General Plugin Configuration
        // # <> - Required
        // # [] - Optional
        this.PREFIX = StringUtils.colorize(mainConfig.getString("PREFIX"));
        this.SILENT_PERMISSION_CHECK = mainConfig.getBoolean("SILENT_PERMISSION_CHECK");
        this.USE_COMMAND_CONFIRMATION = mainConfig.getBoolean("USE_COMMAND_CONFIRMATION");

        // Global Chat Command (/globalchat|global|gc|g <message>)
        // # Sends your message to the whole network
        this.GLOBALCHAT_COOLDOWN = mainConfig.getLong("COMMANDS.GLOBALCHAT_COMMAND.COOLDOWN");
        this.GLOBALCHAT_LOGS_TO_CONSOLE = mainConfig.getBoolean("COMMANDS.GLOBALCHAT_COMMAND.LOGS_GLOBALCHAT_TO_CONSOLE");

        // Hub Command (/hub)
        // # Sends you to hub servers
        this.HUB_SERVERS = mainConfig.getStringList("COMMANDS.HUB_COMMAND.HUB_SERVERS");
    }

    /**
     * Save the main configuration file
     */
    public void save() {
        Configuration mainConfig = configManager.getConfig("config.yml");

        // General Plugin Configuration
        // # <> - Required
        // # [] - Optional
        mainConfig.set("PREFIX", this.PREFIX);
        mainConfig.set("SILENT_PERMISSION_CHECK", this.SILENT_PERMISSION_CHECK);
        mainConfig.set("USE_COMMAND_CONFIRMATION", this.USE_COMMAND_CONFIRMATION);

        // Global Chat Command (/globalchat|global|gc|g <message>)
        // # Sends your message to the whole network
        mainConfig.set("COMMANDS.GLOBALCHAT_COMMAND.COOLDOWN", this.GLOBALCHAT_COOLDOWN);
        mainConfig.set("COMMANDS.GLOBALCHAT_COMMAND.LOGS_GLOBALCHAT_TO_CONSOLE", this.GLOBALCHAT_LOGS_TO_CONSOLE);

        // Hub Command (/hub)
        // # Sends you to hub servers
        mainConfig.set("COMMANDS.HUB_COMMAND.HUB_SERVERS", this.HUB_SERVERS);

        configManager.save("config.yml");
    }
}
