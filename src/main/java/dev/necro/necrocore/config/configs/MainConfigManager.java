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

    private String prefix;
    private boolean silentPermsCheck;
    private boolean useConfirmation;

    private List<String> hubServers;

    private long globalChatCooldown;
    private boolean logsGlobalChatToConsole;

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
        this.silentPermsCheck = mainConfig.getBoolean("use-silent-permission-check");
        this.useConfirmation = mainConfig.getBoolean("use-confirmation");

        this.hubServers = mainConfig.getStringList("hub-servers");

        this.globalChatCooldown = mainConfig.getLong("globalchat-command.cooldown");
        this.logsGlobalChatToConsole = mainConfig.getBoolean("globalchat-command.logs-globalchat-to-console");
    }

    /**
     * Save the main configuration file
     */
    public void save() {
        ConfigManager configManager = new ConfigManager(plugin);
        Configuration mainConfig = configManager.getConfig("config.yml");

        mainConfig.set("prefix", this.prefix);
        mainConfig.set("use-silent-permission-check", this.silentPermsCheck);
        mainConfig.set("use-confirmation", this.useConfirmation);

        mainConfig.set("hub-servers", this.hubServers);

        mainConfig.set("globalchat-command.cooldown", this.globalChatCooldown);
        mainConfig.set("globalchat-command.logs-globalchat-to-console", this.logsGlobalChatToConsole);

        configManager.save("config.yml");
    }
}
