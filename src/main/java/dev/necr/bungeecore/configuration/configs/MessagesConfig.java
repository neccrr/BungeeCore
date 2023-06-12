package dev.necr.bungeecore.configuration.configs;

import dev.necr.bungeecore.BungeeCore;
import dev.necr.bungeecore.utils.StringUtils;
import lombok.Data;
import lombok.experimental.Accessors;
import net.md_5.bungee.config.Configuration;

import java.io.IOException;
import java.util.List;

@Accessors(fluent = true)
@Data
public class MessagesConfig {

    protected final BungeeCore plugin = BungeeCore.getInstance();
    private final Configuration messagesConfig = this.plugin.getConfigManager().loadConfig("messages.yml");

    public MessagesConfig() throws IOException {}

    // General Plugin Message Configuration
    private final String TARGET_NOT_FOUND = StringUtils.colorize(messagesConfig.getString("GENERAL.TARGET_NOT_FOUND"));
    private final String PLAYER_ONLY = StringUtils.colorize(messagesConfig.getString("GENERAL.PLAYER_ONLY"));
    private final String RELOAD_SUCCESSFUL = StringUtils.colorize(messagesConfig.getString("GENERAL.RELOAD_SUCCESSFUL"));

    // Ping Command (/ping [target])
    // # Show your/other latency to the server
    private final String PING_SELF = StringUtils.colorize(messagesConfig.getString("COMMANDS.PING_COMMAND.SELF"));
    private final String PING_OTHER = StringUtils.colorize(messagesConfig.getString("COMMANDS.PING_COMMAND.OTHER"));

    // Global Chat Command (/globalchat|global|gc|g <message>)
    // # Sends your message to the whole network
    private final String GLOBALCHAT_FORMAT = StringUtils.colorize(messagesConfig.getString("COMMANDS.GLOBALCHAT_COMMAND.FORMAT"));

    // Private Message Command (/message|msg|m|whisper|w|tell <target> <message>)
    // # Sends private message to another player
    private final String PRIVATEMESSAGE_SELF = StringUtils.colorize(messagesConfig.getString("COMMANDS.PRIVATEMESSAGE_COMMAND.SELF"));
    private final String PRIVATEMESSAGE_TO = StringUtils.colorize(messagesConfig.getString("COMMANDS.PRIVATEMESSAGE_COMMAND.TO"));
    private final String PRIVATEMESSAGE_FROM = StringUtils.colorize(messagesConfig.getString("COMMANDS.PRIVATEMESSAGE_COMMAND.FROM"));
    private final String PRIVATEMESSAGE_NO_ONE_TO_REPLY = StringUtils.colorize(messagesConfig.getString("COMMANDS.PRIVATEMESSAGE_COMMAND.NO_ONE_TO_REPLY"));

    // Warp Command (/warp|goto <target>)
    // # Sends you to player's current server
    private final String WARP_CONNECT = StringUtils.colorize(messagesConfig.getString("COMMANDS.WARP_COMMAND.CONNECT"));
    private final String WARP_SAME_SERVER = StringUtils.colorize(messagesConfig.getString("COMMANDS.WARP_COMMAND.SAME_SERVER"));

    // Info Command (/info|playerinfo|dox <target>)
    // # Gets some information about the target
    private final List<String> INFO_FORMAT = StringUtils.colorize(messagesConfig.getStringList("COMMANDS.INFO_COMMAND.FORMAT"));

    // Plugin Manager Command
    // # (/bungeecore pluginmanager|pm list) | Lists all loaded plugin
    // # (/bungeecore pluginmanager|pm load <pluginName>) | Loads a plugin
    // # (/bungeecore pluginmanager|pm unload <pluginName>) | Unloads a plugin
    // # (/bungeecore pluginmanager|pm reload <pluginName>) | Reloads a plugin
    private final String PLUGIN_MANAGER_LIST = StringUtils.colorize(messagesConfig.getString("COMMANDS.PLUGIN_MANAGER_COMMAND.LIST"));
    private final String PLUGIN_MANAGER_NOT_FOUND = StringUtils.colorize(messagesConfig.getString("COMMANDS.PLUGIN_MANAGER_COMMAND.NOT_FOUND"));

    // Command Blocker
    private final String COMMAND_BLOCKER_BLOCKED_COMMAND_MESSAGE = StringUtils.colorize(messagesConfig.getString("COMMAND_BLOCKER.BLOCKED_COMMANDS_MESSAGE"));
    private final String COMMAND_BLOCKER_LOGS_TO_CONSOLE_MESSAGE = StringUtils.colorize(messagesConfig.getString("COMMAND_BLOCKER.LOGS_TO_CONSOLE_MESSAGE"));
    private final String COMMAND_BLOCKER_NOTIFY_STAFF_MESSAGE = StringUtils.colorize(messagesConfig.getString("COMMAND_BLOCKER.NOTIFY_STAFF_MESSAGE"));

    public void reload() {
        this.plugin.getConfigManager().reloadConfig("messages.yml");
    }
}
