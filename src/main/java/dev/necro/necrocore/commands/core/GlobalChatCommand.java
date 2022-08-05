package dev.necro.necrocore.commands.core;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.specifier.Greedy;
import dev.necro.necrocore.commands.api.CommandClass;
import dev.necro.necrocore.hooks.LuckPermsHook;
import dev.necro.necrocore.utils.StringUtils;
import dev.necro.necrocore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.HashMap;
import java.util.Map;

public class GlobalChatCommand extends CommandClass {

    Map<String, Long> cooldown = new HashMap<>();

    @CommandMethod("globalchat|gchat|gc|g <message>")
    @CommandDescription("Sends message to the whole server")
    public void globalChatCommand(final @NonNull CommandSender sender, final @NonNull @Argument(value = "message", description = "The text") @Greedy String message) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;

            if (!Utils.checkPermission(sender, "globalchat.use")) {
                return;
            }

            // Global Chat Cooldown
            if (cooldown.containsKey(player.getName()) && !player.hasPermission("necrocore.globalchat.bypasscooldown")) {
                if (cooldown.get(player.getName()) > System.currentTimeMillis()) {
                    long timeLeft =  (cooldown.get(player.getName()) - System.currentTimeMillis()) / 1000;
                    player.sendMessage(new TextComponent(StringUtils.colorize("&cYou need to wait &l" + timeLeft + " &cmore seconds to use global chat again!")));
                    return;
                }
            }

            cooldown.put(player.getName(), System.currentTimeMillis() + (plugin.getMainConfig().getGlobalChatCooldown() * 1000));

            for (final ProxiedPlayer target : ProxyServer.getInstance().getPlayers()) {
                if (target.hasPermission("necrocore.globalchat.read")) {
                    target.sendMessage(new TextComponent(plugin.getMainConfig().getPrefix() + this.getFormattedGlobalChat(player, message)));
                }
            }

            // Logs to console
            if (plugin.getMainConfig().isLogsGlobalChatToConsole()) {
                ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(this.getFormattedGlobalChat(player, message)));
            }

        } else {
            sender.sendMessage(new TextComponent(plugin.getMainConfig().getPrefix() + plugin.getMessagesConfig().getPlayerOnly()));
        }
    }

    private String getFormattedGlobalChat(ProxiedPlayer player, String message) {
        return plugin.getMessagesConfig().getGlobalChatFormat()
                .replace("{player_name}", player.getName())
                .replace("{player_prefix}", LuckPermsHook.getPrefix(player))
                .replace("{player_suffix}", LuckPermsHook.getSuffix(player))
                .replace("{player_server_name}", player.getServer().getInfo().getName().toUpperCase()
                        .replace("BEDWARS-MG1", "BEDWARS")
                        .replace("BEDWARS-MG2", "BEDWARS")
                        .replace("BEDWARS-MG3", "BEDWARS")
                        .replace("BEDWARS-MG4", "BEDWARS")
                        .replace("BEDWARS-EVENT1", "BEDWARS")
                        .replace("BEDWARS-EVENT2", "BEDWARS")
                        .replace("BEDWARS-EVENT3", "BEDWARS"))
                .replace("{message}", StringUtils.colorize(message));
    }
}
