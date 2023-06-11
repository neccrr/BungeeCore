package dev.necr.bungeecore.commands.core;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import dev.necr.bungeecore.commands.api.CommandClass;
import dev.necr.bungeecore.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

@SuppressWarnings("unused")
public class WhereAmICommand extends CommandClass {

    @CommandMethod("whereami")
    @CommandDescription("See the name of the server you currently connected to")
    public void whereAmICommand(final @NonNull CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (player.getServer().getInfo() != null) {
                player.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + StringUtils.colorize("&bYou are currently connected to &6" + player.getServer().getInfo().getName())));
            } else {
                player.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + StringUtils.colorize("&cCannot get the server information! Please try again later.")));
            }

        } else {
            sender.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().PLAYER_ONLY()));
        }
    }
}
