package dev.necro.necrocore.config.configs;

import dev.necro.necrocore.NecroCore;
import dev.necro.necrocore.config.ConfigManager;
import dev.necro.necrocore.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.config.Configuration;

@Getter
@Setter
public class MessagesConfigManager {

    private final NecroCore plugin;

    private String pingSelf, pingOther;

    private String reloadSuccessful;

    private String pluginList, pluginNotFound;

    public MessagesConfigManager(NecroCore plugin) {
        this.plugin = plugin;
        this.reload();
    }

    /**
     * Reload the messages' configuration file
     */
    public void reload() {
        ConfigManager configManager = new ConfigManager(plugin);
        Configuration messagesConfig = configManager.getConfig("messages.yml");

        // Ping Command messages configuration
        this.pingSelf = StringUtils.colorize(messagesConfig.getString("Ping-Command.Ping-Self"));
        this.pingOther = StringUtils.colorize(messagesConfig.getString("Ping-Command.Ping-Other"));

        // Reload messages configuration
        this.reloadSuccessful = StringUtils.colorize(messagesConfig.getString("Reload-Command.Reload-Successful"));

        // Plugin Manager messages configuration
        this.pluginList = StringUtils.colorize(messagesConfig.getString("Plugin-Manager.Plugin-List"));
        this.pluginNotFound = StringUtils.colorize(messagesConfig.getString("Plugin-Manager.Plugin-NotFound"));
    }

    /**
     * Save the messages' configuration file
     */
    public void save() {
        ConfigManager configManager = new ConfigManager(plugin);
        Configuration messagesConfig = configManager.getConfig("config.yml");

        // Ping Command Messages configuration
        messagesConfig.set("Ping-Command.Ping-Other", this.pingSelf);
        messagesConfig.set("Ping-Command.Ping-Other", this.pingOther);

        // Reload messages configuration
        messagesConfig.set("Reload-Command.Reload-Successful", this.reloadSuccessful);

        // Plugin Manager messages configuration
        messagesConfig.set("Plugin-Manager.Plugin-List", this.pluginList);
        messagesConfig.set("Plugin-Manager.Plugin-NotFound", this.pluginNotFound);

        configManager.save("messages.yml");
    }
}