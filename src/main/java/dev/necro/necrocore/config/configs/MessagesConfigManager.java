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

    private String pingSelf, pingOther;

    private String targetNotFound, reloadSuccessful, playerOnly;

    private String pluginList, pluginNotFound;

    private String gotoConnected, gotoSameServer;

    private List<String> infoMessage;

    private String globalChatFormat;

    private String privMessageSelf, privMessageTo, privMessageFrom, privMessageNoneToReply;

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

        // General plugin messages configuration
        this.targetNotFound = StringUtils.colorize(messagesConfig.getString("General.Target-NotFound"));
        this.reloadSuccessful = StringUtils.colorize(messagesConfig.getString("General.Reload-Successful"));
        this.playerOnly = StringUtils.colorize(messagesConfig.getString("General.Player-Only"));

        // Ping Command messages configuration
        this.pingSelf = StringUtils.colorize(messagesConfig.getString("Ping-Command.Ping-Self"));
        this.pingOther = StringUtils.colorize(messagesConfig.getString("Ping-Command.Ping-Other"));

        // Goto Command Messages configuration
        this.gotoConnected = StringUtils.colorize(messagesConfig.getString("Goto-Command.Goto-Connected"));
        this.gotoSameServer = StringUtils.colorize(messagesConfig.getString("Goto-Command.Goto-SameServer"));

        // Plugin Manager messages configuration
        this.pluginList = StringUtils.colorize(messagesConfig.getString("Plugin-Manager.Plugin-List"));
        this.pluginNotFound = StringUtils.colorize(messagesConfig.getString("Plugin-Manager.Plugin-NotFound"));

        // Info Command messages configuration
        this.infoMessage = StringUtils.colorize(messagesConfig.getStringList("Info-Command.Info-Message"));

        // Global Chat Command messages configuration
        this.globalChatFormat = StringUtils.colorize(messagesConfig.getString("GlobalChat-Command.GlobalChat-Format"));

        // Private Message messages configuration
        this.privMessageSelf = StringUtils.colorize(messagesConfig.getString("PrivateMessage-Command.Message-Self"));
        this.privMessageTo = StringUtils.colorize(messagesConfig.getString("PrivateMessage-Command.Message-To"));
        this.privMessageFrom = StringUtils.colorize(messagesConfig.getString("PrivateMessage-Command.Message-From"));
        this.privMessageNoneToReply = StringUtils.colorize(messagesConfig.getString("PrivateMessage-Command.No-One-To-Reply"));

    }

    /**
     * Save the messages' configuration file
     */
    public void save() {
        ConfigManager configManager = new ConfigManager(plugin);
        Configuration messagesConfig = configManager.getConfig("messages.yml");

        // General messages configuration
        messagesConfig.set("General.Target-NotFound", this.targetNotFound);
        messagesConfig.set("General.Reload-Successful", this.reloadSuccessful);
        messagesConfig.set("General.Player-Only", this.playerOnly);

        // Ping Command Messages configuration
        messagesConfig.set("Ping-Command.Ping-Other", this.pingSelf);
        messagesConfig.set("Ping-Command.Ping-Other", this.pingOther);

        // Goto Command Messages configuration
        messagesConfig.set("Goto-Command.Goto-Connected", this.gotoConnected);
        messagesConfig.set("Goto-Command.Goto-SameServer", this.gotoSameServer);

        // Plugin Manager messages configuration
        messagesConfig.set("Plugin-Manager.Plugin-List", this.pluginList);
        messagesConfig.set("Plugin-Manager.Plugin-NotFound", this.pluginNotFound);

        // Info Command messages configuration
        messagesConfig.set("Info-Command.Info-Message", this.infoMessage);

        // Global Chat messages configuration
        messagesConfig.set("GlobalChat-Command.GlobalChat-Format", this.globalChatFormat);

        // Private Message messages configuration
        messagesConfig.set("PrivateMessage-Command.Message-Self", this.privMessageSelf);
        messagesConfig.set("PrivateMessage-Command.Message-To", this.privMessageTo);
        messagesConfig.set("PrivateMessage-Command.Message-From", this.privMessageFrom);

        configManager.save("messages.yml");
    }
}