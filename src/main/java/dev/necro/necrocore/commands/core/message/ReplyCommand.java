package dev.necro.necrocore.commands.core.message;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.specifier.Greedy;
import dev.necro.necrocore.commands.api.CommandClass;
import dev.necro.necrocore.hooks.LuckPermsHook;
import dev.necro.necrocore.managers.ReplyManager;
import dev.necro.necrocore.utils.StringUtils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ReplyCommand extends CommandClass {

    @CommandMethod("reply|r <message>")
    public void replyCommand(final @NonNull CommandSender sender, final @NonNull @Argument(value = "message", description = "The message to send") @Greedy String message) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(ReplyManager.get(player.getUniqueId()));

            if (!ReplyManager.isInReply(player.getUniqueId())) {
                player.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + plugin.getMessagesConfig().getPRIVATEMESSAGE_NO_ONE_TO_REPLY()));
                return;
            }

            player.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + plugin.getMessagesConfig().getPRIVATEMESSAGE_TO()
                    .replace("{player_name}", player.getName())
                    .replace("{player_prefix}", LuckPermsHook.getPrefix(player))
                    .replace("{player_suffix}", LuckPermsHook.getSuffix(player))
                    .replace("{target_name}", target.getName())
                    .replace("{target_prefix}", LuckPermsHook.getPrefix(target))
                    .replace("{target_suffix}", LuckPermsHook.getSuffix(target))
                    .replace("{message}", StringUtils.colorize(message))));

            target.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + plugin.getMessagesConfig().getPRIVATEMESSAGE_FORM()
                    .replace("{player_name}", player.getName())
                    .replace("{player_prefix}", LuckPermsHook.getPrefix(player))
                    .replace("{player_suffix}", LuckPermsHook.getSuffix(player))
                    .replace("{target_name}", target.getName())
                    .replace("{target_prefix}", LuckPermsHook.getPrefix(target))
                    .replace("{target_suffix}", LuckPermsHook.getSuffix(target))
                    .replace("{message}", StringUtils.colorize(message))));
        } else {
            sender.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + plugin.getMessagesConfig().getPLAYER_ONLY()));
        }
    }
}