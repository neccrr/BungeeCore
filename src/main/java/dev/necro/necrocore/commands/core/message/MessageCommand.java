package dev.necro.necrocore.commands.core.message;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.specifier.Greedy;
import dev.necro.necrocore.commands.api.CommandClass;
import dev.necro.necrocore.hooks.LuckPermsHook;
import dev.necro.necrocore.managers.ReplyManager;
import dev.necro.necrocore.utils.StringUtils;
import dev.necro.necrocore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.UUID;

@SuppressWarnings("unused")
public class MessageCommand extends CommandClass {

    @CommandMethod("message|msg|m|whisper|w|tell <target> <message>")
    @CommandDescription("Sends private message to another player")
    public void messageCommand(final @NonNull CommandSender sender, final @NonNull @Argument(value = "target", description = "The target player", suggestions = "players") String targetName, final @NonNull @Argument(value = "message", description = "The message to send") @Greedy String message) {
        if (sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            ProxiedPlayer target = ProxyServer.getInstance().getPlayer(targetName);

            if (!Utils.checkPermission(sender, "privatemessage.message")) {
                return;
            }

            if (player.getName().equalsIgnoreCase(targetName)) {
                player.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().PRIVATEMESSAGE_SELF()));
                return;
            }

            if (target == null) {
                player.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().TARGET_NOT_FOUND()
                        .replace("{target_name}", targetName)));
                return;
            }

            player.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().PRIVATEMESSAGE_TO()
                    .replace("{player_name}", player.getName())
                    .replace("{player_prefix}", LuckPermsHook.getPrefix(player))
                    .replace("{player_suffix}", LuckPermsHook.getSuffix(player))
                    .replace("{target_name}", target.getName())
                    .replace("{target_prefix}", LuckPermsHook.getPrefix(target))
                    .replace("{target_suffix}", LuckPermsHook.getSuffix(target))
                    .replace("{message}", StringUtils.colorize(message))));

            target.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().PRIVATEMESSAGE_FROM()
                    .replace("{player_name}", player.getName())
                    .replace("{player_prefix}", LuckPermsHook.getPrefix(player))
                    .replace("{player_suffix}", LuckPermsHook.getSuffix(player))
                    .replace("{target_name}", target.getName())
                    .replace("{target_prefix}", LuckPermsHook.getPrefix(target))
                    .replace("{target_suffix}", LuckPermsHook.getSuffix(target))
                    .replace("{message}", StringUtils.colorize(message))));

            this.reply(player.getUniqueId(), target.getUniqueId());

        } else {
            sender.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().PLAYER_ONLY()));
        }
    }

    /**
     * Checks if the player/target already exists in the reply data
     * <p>
     * <p>
     * If the player/target already exists in the reply data then
     * it will renew the reply data by removing them from the reply data
     * and add them again together
     * <p>
     * <p>
     * If the player/target is not exists in the reply data then
     * it will add them to the reply data together
     *
     * @param playerUUID The player's UUID
     * @param targetUUID The target's UUID
     */
    private void reply(UUID playerUUID, UUID targetUUID) {
        // Checks if the player is in reply data
        if (!ReplyManager.isInReply(playerUUID)) {
            ReplyManager.add(playerUUID, targetUUID);
        } else {
            ReplyManager.remove(playerUUID);
            ReplyManager.add(playerUUID, targetUUID);
        }

        // Checks if the target is in reply data
        if (!ReplyManager.isInReply(targetUUID)) {
            ReplyManager.add(targetUUID, playerUUID);
        } else {
            ReplyManager.remove(targetUUID);
            ReplyManager.add(targetUUID, playerUUID);
        }
    }
}