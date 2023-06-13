package dev.necr.bungeecore.configuration.configs;

import dev.necr.bungeecore.BungeeCore;
import lombok.Data;
import lombok.experimental.Accessors;
import net.md_5.bungee.config.Configuration;

import java.io.IOException;
import java.util.List;

@Accessors(fluent = true)
@Data
public class MainConfig {

    protected final BungeeCore plugin = BungeeCore.getInstance();
    private final Configuration mainConfig = this.plugin.getConfigManager().loadConfig("config.yml");

    public MainConfig() throws IOException {}

    // General Plugin Configuration
    private final String PREFIX = mainConfig.getString("PREFIX");

    private final boolean SILENT_PERMISSION_CHECK = mainConfig.getBoolean("SILENT_PERMISSION_CHECK");
    private final boolean USE_COMMAND_CONFIRMATION = mainConfig.getBoolean("USE_COMMAND_CONFIRMATION");

    // Commands Configuration
    // Global Chat
    private final long GLOBALCHAT_COOLDOWN = mainConfig.getLong("COMMANDS.GLOBALCHAT_COMMAND.COOLDOWN");
    private final boolean GLOBALCHAT_LOGS_TO_CONSOLE = mainConfig.getBoolean("COMMANDS.GLOBALCHAT_COMMAND.LOGS_TO_CONSOLE");

    // Hub cmd
    private final List<String> HUB_SERVERS = mainConfig.getStringList("COMMANDS.HUB_COMMAND.HUB_SERVERS");

    // Private message
    private final boolean PRIVATE_MESSAGE_LOGS_TO_CONSOLE = mainConfig.getBoolean("COMMANDS.PRIVATE_MESSAGE_COMMAND.LOGS_TO_CONSOLE");

    // Command Blocker Configuration
    private final boolean COMMAND_BLOCKER_ENABLED = mainConfig.getBoolean("COMMAND_BLOCKER.ENABLED");
    private final boolean COMMAND_BLOCKER_LOGS_TO_CONSOLE = mainConfig.getBoolean("COMMAND_BLOCKER.LOGS_TO_CONSOLE");
    private final boolean COMMAND_BLOCKER_NOTIFY_STAFF = mainConfig.getBoolean("COMMAND_BLOCKER.NOTIFY_STAFF");
    private final List<String> COMMAND_BLOCKER_BLOCKED_COMMANDS = mainConfig.getStringList("COMMAND_BLOCKER.BLOCKED_COMMANDS");

    public void reload() {
        this.plugin.getConfigManager().reloadConfig("config.yml");
    }
}
