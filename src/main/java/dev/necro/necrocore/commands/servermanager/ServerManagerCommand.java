package dev.necro.necrocore.commands.servermanager;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import dev.necro.necrocore.commands.api.CommandClass;
import dev.necro.necrocore.managers.servermanager.ServerManager;
import dev.necro.necrocore.utils.StringUtils;
import dev.necro.necrocore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.net.SocketAddress;

@SuppressWarnings("unused")
public class ServerManagerCommand extends CommandClass {

    @CommandMethod("necrocore servermanager|sm list")
    @CommandDescription("Lists all server instances connected to the current proxy")
    public void listCommand(final @NonNull CommandSender sender) {
        if (!Utils.checkPermission(sender, "servermanager.list")) {
            return;
        }

        sender.sendMessage(new TextComponent(StringUtils.colorize(plugin.getMainConfig().PREFIX() + "Servers: &7(&b" + ServerManager.getServerCount() + "&7): &b" + ServerManager.getServerList())));
    }

    @CommandMethod("necrocore servermanager|sm info <serverName>")
    @CommandDescription("Gets information about the inputted server instances")
    public void infoCommand(final @NonNull CommandSender sender, final @NonNull @Argument(value = "serverName", description = "The server name") String serverName) {
        if (!Utils.checkPermission(sender, "servermanager.info")) {
            return;
        }

        ServerInfo serverInfo = ServerManager.getServerInfo(serverName);

        // TODO: server info messages
    }

    @CommandMethod("necrocore servermanager|sm add <serverName> <hostName>")
    @CommandDescription("Connects a new server instances to the proxy")
    public void addCommand(final @NonNull CommandSender sender, final @NonNull @Argument(value = "serverName", description = "The server name") String serverName, final @NonNull @Argument(value = "hostName", description = "The server's hostname") String hostName) {
        if (!Utils.checkPermission(sender, "servermanager.add")) {
            return;
        }

        SocketAddress address = ServerManager.parseSocketAddress(hostName);
        if (address == null) {
            sender.sendMessage(new TextComponent(StringUtils.colorize(plugin.getMainConfig().PREFIX() + "&cInvalid address! Correct address is: &fhostname:port (127.0.0.1:25565)")));
        }

        ServerInfo serverInfo = ProxyServer.getInstance().constructServerInfo(serverName, address, "", false);

        ServerManager.addServer(serverInfo);
        sender.sendMessage(new TextComponent(StringUtils.colorize(plugin.getMainConfig().PREFIX() + "&bAdded an server with the name &e" + serverInfo.getName() + " &band address &e" + serverInfo.getSocketAddress())));
    }

    @CommandMethod("necrocore servermanager|sm remove <serverName>")
    @CommandDescription("Removes an server instances to the proxy")
    public void removeCommand(final @NonNull CommandSender sender, final @NonNull @Argument(value = "serverName", description = "The server name") String serverName) {
        if (!Utils.checkPermission(sender, "servermanager.remove")) {
            return;
        }

        if (!ServerManager.isServerExists(serverName)) {
            sender.sendMessage(TextComponent.fromLegacyText(plugin.getMainConfig().PREFIX() + "&cThe server &l" + serverName + "&r&c does not exist."));
            return;
        }

        ServerInfo serverInfo = ProxyServer.getInstance().getServers().get(serverName);

        ServerManager.removeServer(serverInfo.getName());
        sender.sendMessage(new TextComponent(StringUtils.colorize(plugin.getMainConfig().PREFIX() + "&bRemoved an server with the name &e" + serverInfo.getName() + " &band address &e" + serverInfo.getSocketAddress())));
    }

}
