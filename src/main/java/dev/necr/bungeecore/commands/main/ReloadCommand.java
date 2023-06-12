package dev.necr.bungeecore.commands.main;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import dev.necr.bungeecore.commands.api.CommandClass;
import dev.necr.bungeecore.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

@SuppressWarnings("unused")
public class ReloadCommand extends CommandClass {

    @CommandMethod("bungeecore reload")
    @CommandDescription("Reloads the plugin's main configuration and message configuration")
    public void reloadCommand(final @NonNull CommandSender sender) {
        long millis = System.currentTimeMillis();

        // Reloads the configuration and messages file
        plugin.getMainConfig().reload();
        plugin.getMessagesConfig().reload();

        // Tell the command sender that the files have been reloaded
        sender.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().RELOAD_SUCCESSFUL()));
        sender.sendMessage(new TextComponent(StringUtils.colorize("&a(" + (System.currentTimeMillis() - millis) + "ms)")));
    }
}
