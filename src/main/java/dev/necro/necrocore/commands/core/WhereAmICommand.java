package dev.necro.necrocore.commands.core;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import dev.necro.necrocore.commands.api.CommandClass;
import dev.necro.necrocore.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class WhereAmICommand extends CommandClass {

    @CommandMethod("whereami")
    @CommandDescription("See the name of the server you currently connected to")
    public void whereAmICommand(final @NonNull CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (player.getServer().getInfo() != null) {
                player.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + StringUtils.colorize("&bYou are currently connected to &6" + player.getServer().getInfo().getName())));
            } else {
                player.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + StringUtils.colorize("&cCannot get the server information! Please try again later.")));
            }

        } else {
            sender.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + plugin.getMessagesConfig().getPLAYER_ONLY()));
        }
    }
}
