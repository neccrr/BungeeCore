package dev.necr.bungeecore.commands.core;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import dev.necr.bungeecore.commands.api.CommandClass;
import dev.necr.bungeecore.utils.StringUtils;
import dev.necr.bungeecore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

@SuppressWarnings("unused")
public class PingCommand extends CommandClass {

    @CommandMethod("ping")
    @CommandDescription("Show your latency to the server")
    public void pingCommand (final @NonNull CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            if (!Utils.checkPermission(sender, "commands.ping")) {
                return;
            }

            ProxiedPlayer player = (ProxiedPlayer) sender;
            int ping = player.getPing();

            sender.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().PING_SELF()
                    .replace("{ping}", String.valueOf(ping))));
        } else {
            pingOtherCommand(sender, "");
        }
    }

    @CommandMethod("ping [target]")
    @CommandDescription("Show your/others latency to the server")
    public void pingOtherCommand (final @NonNull CommandSender sender, final @Nullable @Argument(value = "target", description = "The target player", suggestions = "players") String targetName) {
        if (!sender.hasPermission("bungeecore.commands.ping.other")) {
            pingCommand(sender);
            return;
        }

        if (targetName == null && sender instanceof ProxiedPlayer) {
            pingCommand(sender);
            return;
        }

        if (targetName != null) {
            if (targetName.equals(sender.getName()) && sender instanceof ProxiedPlayer) {
                pingCommand(sender);
                return;
            }
        }

        TargetsCallback targets = this.getTargets(sender, targetName);

        if (targets.notifyIfEmpty()) {
            sender.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + StringUtils.colorize("&cNo targets found!")));
            return;
        }
        if (targets.size() > 1) {
            sender.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + StringUtils.colorize("&cYou can only check one player at a time!")));
            return;
        }

        targets.stream().findFirst().ifPresent(target -> {
            int targetPing = target.getPing();
            sender.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().PING_OTHER()
                    .replace("{target_ping}", String.valueOf(targetPing))
                    .replace("{target_name}", target.getName())));
        });

    }

}
