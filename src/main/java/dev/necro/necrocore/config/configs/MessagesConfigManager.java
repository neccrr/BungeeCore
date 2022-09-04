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
public class MessagesConfigManager {

    private final NecroCore plugin;
    private final ConfigManager configManager;

    private String TARGET_NOT_FOUND, RELOAD_SUCCESSFUL, PLAYER_ONLY;

    private String PING_SELF, PING_OTHER;

    private String PLUGIN_MANAGER_LIST, PLUGIN_MANAGER_NOT_FOUND;

    private String WARP_CONNECT, WARP_SAME_SERVER;

    private List<String> INFO_FORMAT;

    private String GLOBALCHAT_FORMAT;

    private String PRIVATEMESSAGE_SELF, PRIVATEMESSAGE_TO, PRIVATEMESSAGE_FORM, PRIVATEMESSAGE_NO_ONE_TO_REPLY;

    public MessagesConfigManager(NecroCore plugin) {
        this.plugin = plugin;
        this.configManager = new ConfigManager(plugin);
        this.reload();
    }

    /**
     * Reload the messages' configuration file
     */
    public void reload() {
        Configuration messagesConfig = configManager.getConfig("messages.yml");

        // General Plugin Message Configuration
        // # <> - Required
        // # [] - Optional
        this.TARGET_NOT_FOUND = StringUtils.colorize(messagesConfig.getString("GENERAL.TARGET_NOT_FOUND"));
        this.PLAYER_ONLY = StringUtils.colorize(messagesConfig.getString("GENERAL.PLAYER_ONLY"));
        this.RELOAD_SUCCESSFUL = StringUtils.colorize(messagesConfig.getString("GENERAL.RELOAD_SUCCESSFUL"));

        // Ping Command (/ping [target])
        // # Show your/other latency to the server
        this.PING_SELF = StringUtils.colorize(messagesConfig.getString("COMMANDS.PING_COMMAND.SELF"));
        this.PING_OTHER = StringUtils.colorize(messagesConfig.getString("COMMANDS.PING_COMMAND.OTHER"));

        // Global Chat Command (/globalchat|global|gc|g <message>)
        // # Sends your message to the whole network
        this.GLOBALCHAT_FORMAT = StringUtils.colorize(messagesConfig.getString("COMMANDS.GLOBALCHAT_COMMAND.FORMAT"));

        // Private Message Command (/message|msg|m|whisper|w|tell <target> <message>)
        // # Sends private message to another player
        this.PRIVATEMESSAGE_SELF = StringUtils.colorize(messagesConfig.getString("COMMANDS.PRIVATEMESSAGE_COMMAND.SELF"));
        this.PRIVATEMESSAGE_TO = StringUtils.colorize(messagesConfig.getString("COMMANDS.PRIVATEMESSAGE_COMMAND.TO"));
        this.PRIVATEMESSAGE_FORM = StringUtils.colorize(messagesConfig.getString("COMMANDS.PRIVATEMESSAGE_COMMAND.FROM"));
        this.PRIVATEMESSAGE_NO_ONE_TO_REPLY = StringUtils.colorize(messagesConfig.getString("COMMANDS.PRIVATEMESSAGE_COMMAND.NO_ONE_TO_REPLY"));

        // Warp Command (/warp|goto <target>)
        // # Sends you to player's current server
        this.WARP_CONNECT = StringUtils.colorize(messagesConfig.getString("COMMANDS.WARP_COMMAND.CONNECT"));
        this.WARP_SAME_SERVER = StringUtils.colorize(messagesConfig.getString("COMMANDS.WARP_COMMAND.SAME_SERVER"));

        // Info Command (/info|playerinfo|dox <target>)
        // # Gets some information about the target | "doxing btw" -jiternos
        this.INFO_FORMAT = StringUtils.colorize(messagesConfig.getStringList("COMMANDS.INFO_COMMAND.FORMAT"));

        // Plugin Manager Command
        // # (/necrocore pluginmanager|pm list) | Lists all loaded plugin
        // # (/necrocore pluginmanager|pm load <pluginName>) | Loads a plugin
        // # (/necrocore pluginmanager|pm unload <pluginName>) | Unloads a plugin
        // # (/necrocore pluginmanager|pm reload <pluginName>) | Reloads a plugin
        this.PLUGIN_MANAGER_LIST = StringUtils.colorize(messagesConfig.getString("COMMANDS.PLUGIN_MANAGER_COMMAND.LIST"));
        this.PLUGIN_MANAGER_NOT_FOUND = StringUtils.colorize(messagesConfig.getString("COMMANDS.PLUGIN_MANAGER_COMMAND.NOT_FOUND"));
    }

    /**
     * Save the messages' configuration file
     */
    public void save() {
        Configuration messagesConfig = configManager.getConfig("messages.yml");

        // General Plugin Message Configuration
        // # <> - Required
        // # [] - Optional
        messagesConfig.set("GENERAL.TARGET_NOT_FOUND", this.TARGET_NOT_FOUND);
        messagesConfig.set("GENERAL.PLAYER_ONLY", this.PLAYER_ONLY);
        messagesConfig.set("GENERAL.RELOAD_SUCCESSFUL", this.RELOAD_SUCCESSFUL);

        // Ping Command (/ping [target])
        // # Show your/other latency to the server
        messagesConfig.set("COMMANDS.PING_COMMAND.SELF", this.PING_SELF);
        messagesConfig.set("COMMANDS.PING_COMMAND.OTHER", this.PING_OTHER);

        // Global Chat Command (/globalchat|global|gc|g <message>)
        // # Sends your message to the whole network
        messagesConfig.set("COMMANDS.GLOBALCHAT_COMMAND.FORMAT", this.GLOBALCHAT_FORMAT);

        // Private Message Command (/message|msg|m|whisper|w|tell <target> <message>)
        // # Sends private message to another player
        messagesConfig.set("COMMANDS.PRIVATEMESSAGE_COMMAND.SELF", this.PRIVATEMESSAGE_SELF);
        messagesConfig.set("COMMANDS.PRIVATEMESSAGE_COMMAND.TO", this.PRIVATEMESSAGE_TO);
        messagesConfig.set("COMMANDS.PRIVATEMESSAGE_COMMAND.FROM", this.PRIVATEMESSAGE_FORM);
        messagesConfig.set("COMMANDS.PRIVATEMESSAGE_COMMAND.NO_ONE_TO_REPLY", this.PRIVATEMESSAGE_NO_ONE_TO_REPLY);

        // Warp Command (/warp|goto <target>)
        // # Sends you to player's current server
        messagesConfig.set("COMMANDS.WARP_COMMAND.CONNECT", this.WARP_CONNECT);
        messagesConfig.set("COMMANDS.WARP_COMMAND.SAME_SERVER", this.WARP_SAME_SERVER);

        // Info Command (/info|playerinfo|dox <target>)
        // # Gets some information about the target | "doxing btw" -jiternos
        messagesConfig.set("COMMANDS.INFO_COMMAND.FORMAT", this.INFO_FORMAT);

        // Plugin Manager Command
        // # (/necrocore pluginmanager|pm list) | Lists all loaded plugin
        // # (/necrocore pluginmanager|pm load <pluginName>) | Loads a plugin
        // # (/necrocore pluginmanager|pm unload <pluginName>) | Unloads a plugin
        // # (/necrocore pluginmanager|pm reload <pluginName>) | Reloads a plugin
        messagesConfig.set("COMMANDS.PLUGIN_MANAGER_COMMAND.LIST", this.PLUGIN_MANAGER_LIST);
        messagesConfig.set("COMMANDS.PLUGIN_MANAGER_COMMAND.NOT_FOUND", this.PLUGIN_MANAGER_NOT_FOUND);

        configManager.save("messages.yml");
    }
}