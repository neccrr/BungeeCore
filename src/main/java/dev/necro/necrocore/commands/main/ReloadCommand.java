package dev.necro.necrocore.commands.main;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import dev.necro.necrocore.commands.api.CommandClass;
import dev.necro.necrocore.config.configs.MainConfigManager;
import dev.necro.necrocore.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.IOException;

public class ReloadCommand extends CommandClass {

    @CommandMethod("necrocore reload")
    @CommandDescription("Reloads the plugin's main configuration and message configuration")
    public void reloadCommand(final @NonNull CommandSender sender) {
        long millis = System.currentTimeMillis();

        // Reloads the configuration and messages file
        try {
            plugin.getMainConfigManager().reload();
            plugin.getMessagesConfigManager().reload();
        } catch (IOException e) {
            plugin.getLogger().severe("Cannot reload the Configuration!");
            e.printStackTrace();
        }


        // Tell the command sender that the files have been reloaded
        sender.sendMessage(new TextComponent(plugin.getMainConfigManager().getPREFIX() + plugin.getMessagesConfigManager().getRELOAD_SUCCESSFUL()));
        sender.sendMessage(new TextComponent(StringUtils.colorize("&a(" + (System.currentTimeMillis() - millis) + "ms)")));
    }
}
