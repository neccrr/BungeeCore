package dev.necr.bungeecore.configuration.configs;

import dev.necr.bungeecore.BungeeCore;
import lombok.Getter;
import lombok.experimental.Accessors;
import net.md_5.bungee.config.Configuration;

import java.io.IOException;
import java.util.List;

@Accessors(fluent = true)
@Getter
public class MainConfig {

    protected final BungeeCore plugin = BungeeCore.getInstance();
    private final Configuration mainConfig = this.plugin.getConfigManager().loadConfig("config.yml");

    public MainConfig() throws IOException {}

    private final String PREFIX = mainConfig.getString("PREFIX");

    private final boolean SILENT_PERMISSION_CHECK = mainConfig.getBoolean("SILENT_PERMISSION_CHECK");
    private final boolean USE_COMMAND_CONFIRMATION = mainConfig.getBoolean("USE_COMMAND_CONFIRMATION");

    private final long GLOBALCHAT_COOLDOWN = mainConfig.getLong("COMMANDS.GLOBALCHAT_COMMAND.COOLDOWN");
    private final boolean GLOBALCHAT_LOGS_TO_CONSOLE = mainConfig.getBoolean("COMMANDS.GLOBALCHAT_COMMAND.LOGS_GLOBALCHAT_TO_CONSOLE");

    private final List<String> HUB_SERVERS = mainConfig.getStringList("COMMANDS.HUB_COMMAND.HUB_SERVERS");

    public void reload() {
        this.plugin.getConfigManager().reloadConfig("config.yml");
    }
}
