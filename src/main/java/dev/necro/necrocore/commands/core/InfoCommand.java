package dev.necro.necrocore.commands.core;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import dev.necro.necrocore.commands.api.CommandClass;
import dev.necro.necrocore.hooks.LuckPermsHook;
import dev.necro.necrocore.utils.StringUtils;
import dev.necro.necrocore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class InfoCommand extends CommandClass {

    @CommandMethod("info|playerinfo|dox [target]")
    @CommandDescription("Gets some information about the target")
    public void infoCommand(final @NonNull CommandSender sender, final @NonNull @Argument(value = "target", description = "The target player", defaultValue = "self", suggestions = "players") String targetName) {
        if (!Utils.checkPermission(sender, "command.info")) {
            return;
        }

        TargetsCallback targets = this.getTargets(sender, targetName);
        if (targets.notifyIfEmpty()) {
            sender.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + StringUtils.colorize("&cNo targets found!")));
            return;
        }
        if (targets.size() > 1) {
            sender.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + StringUtils.colorize("&cYou can only check one player at a time!")));
            return;
        }

        targets.stream().findFirst().ifPresent(target -> {
            for (String result : plugin.getMessagesConfig().getINFO_FORMAT()) {
                sender.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + result
                        .replace("{target_name}", target.getName())
                        .replace("{target_uuid}", String.valueOf(target.getUniqueId()))
                        .replace("{target_rank}", LuckPermsHook.getGroupDisplayName(target))
                        .replace("{target_prefix}", LuckPermsHook.getPrefix(target))
                        .replace("{target_suffix}", LuckPermsHook.getSuffix(target))
                        .replace("{target_server_name}", target.getServer().getInfo().getName())
                        .replace("{target_ping}", String.valueOf(target.getPing()))
                        .replace("{target_ip}", String.valueOf(target.getSocketAddress()))
                        .replace("{target_client_version}", Utils.getClientVersion(target.getPendingConnection().getVersion()))
                        .replace("{target_protocolNumber}", String.valueOf(target.getPendingConnection().getVersion()))
                        ));
            }
        });
    }
}
