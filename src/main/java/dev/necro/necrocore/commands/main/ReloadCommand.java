package dev.necro.necrocore.commands.main;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import dev.necro.necrocore.commands.api.CommandClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ReloadCommand extends CommandClass {

    @CommandMethod("necrocore reload")
    @CommandDescription("Reloads the plugin's main configuration and message configuration")
    public void reloadCommand(final @NonNull CommandSender sender) {
        // Reloads the configuration and messages file
        plugin.getMainConfig().reload();
        plugin.getMessagesConfig().reload();

        // Tell the command sender that the files have been reloaded
        sender.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + plugin.getMessagesConfig().getRELOAD_SUCCESSFUL()));
    }
}
