package dev.necr.bungeecore.listeners;

import dev.necr.bungeecore.BungeeCore;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ChatEvent;
import net.md_5.bungee.api.event.TabCompleteEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

@RequiredArgsConstructor
public class CommandBlockerListener implements Listener {

    private final BungeeCore plugin;

    @EventHandler
    public void onPlayerChat(ChatEvent event) {
        ProxiedPlayer target = (ProxiedPlayer) event.getSender();
        String message = event.getMessage();

        if (message.startsWith("/")) {
            String command = message.split(" ")[0].substring(1); // Remove the leading "/"
            if (plugin.getMainConfig().COMMAND_BLOCKER_BLOCKED_COMMANDS().contains(command) && !target.hasPermission("bungeecore.commandblocker.bypass")) {
                event.setCancelled(true);
                target.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().COMMAND_BLOCKER_BLOCKED_COMMAND_MESSAGE()));

                if (plugin.getMainConfig().COMMAND_BLOCKER_LOGS_TO_CONSOLE()) {
                    ProxyServer.getInstance().getConsole().sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().COMMAND_BLOCKER_NOTIFY_STAFF_MESSAGE()
                            .replace("{target_name}", target.getName())
                            .replace("{blocked_command}", command)
                    ));
                }

                if (plugin.getMainConfig().COMMAND_BLOCKER_NOTIFY_STAFF()) {
                    for (final ProxiedPlayer staff : ProxyServer.getInstance().getPlayers()) {
                        if (staff.hasPermission("bungeecore.commandblocker.notify")) {
                            staff.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().COMMAND_BLOCKER_NOTIFY_STAFF_MESSAGE()
                                    .replace("{target_name}", target.getName())
                                    .replace("{blocked_command}", command)
                            ));
                        }
                    }
                }
            }
        }
    }

    @EventHandler
    public void onTabComplete(TabCompleteEvent event) {
        String input = event.getCursor();
        if (input.startsWith("/")) {
            String command = input.split(" ")[0].substring(1); // Remove the leading "/"
            if (plugin.getMainConfig().COMMAND_BLOCKER_BLOCKED_COMMANDS().contains(command)) {
                event.setCancelled(true);
            }
        }
    }
}

