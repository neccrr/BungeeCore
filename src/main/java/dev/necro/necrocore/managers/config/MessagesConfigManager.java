package dev.necro.necrocore.managers.config;

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

    private String pingSelf;
    private String pingOther;

    private String reloadSuccessful;

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

        // Ping Command Messages
        this.pingSelf = StringUtils.colorize(messagesConfig.getString("Ping-Command.Ping-Self"));
        this.pingOther = StringUtils.colorize(messagesConfig.getString("Ping-Command.Ping-Other"));

        // Reloads messages
        this.reloadSuccessful = StringUtils.colorize(messagesConfig.getString("Reload-Command.Reload-Successful"));
    }

    /**
     * Save the messages' configuration file
     */
    public void save() {
        ConfigManager configManager = new ConfigManager(plugin);
        Configuration messagesConfig = configManager.getConfig("config.yml");

        // Ping Command Messages
        messagesConfig.set("Ping-Command.Ping-Other", this.pingSelf);
        messagesConfig.set("Ping-Command.Ping-Other", this.pingOther);

        // Reloads messages
        messagesConfig.set("Reload-Command.Reload-Successful", this.reloadSuccessful);

        configManager.save("messages.yml");
    }
}