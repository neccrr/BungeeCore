package dev.necro.necrocore.commands.core;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import dev.necro.necrocore.commands.api.CommandClass;
import dev.necro.necrocore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

@SuppressWarnings("unused")
public class WarpCommand extends CommandClass {

    @CommandMethod("warp|warpto|goto <target>")
    @CommandDescription("Sends you to player's current server")
    public void warpCommand(final @NonNull CommandSender sender, final @NonNull @Argument(value = "target", description = "The target player", suggestions = "players") String targetName) {
        if (sender instanceof ProxiedPlayer) {
            if (!Utils.checkPermission(sender, "commands.goto")) {
                return;
            }

            ProxiedPlayer player = (ProxiedPlayer) sender;
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);

            if (target == null) {
                player.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().TARGET_NOT_FOUND()
                        .replace("{target_name}", targetName)));
            } else {
                ServerInfo targetServer = target.getServer().getInfo();
                ServerInfo playerServer = player.getServer().getInfo();

                if (playerServer != targetServer)  {
                    player.connect(targetServer);
                    player.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().WARP_CONNECT()
                            .replace("{target_name}", target.getName())
                            .replace("{target_server_name}", target.getServer().getInfo().getName())));
                } else {
                    // Already on same server exception
                    player.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().WARP_SAME_SERVER()
                            .replace("{target_name}", target.getName())
                            .replace("{target_server_name}", target.getServer().getInfo().getName())));
                }
            }

        } else {
            sender.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().PLAYER_ONLY()));
        }
    }

}
