package dev.necr.bungeecore.managers.servermanager;

import dev.necr.bungeecore.utils.StringUtils;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.net.InetSocketAddress;
import java.net.SocketAddress;

/**
 * Utilities for managing Proxy server instances.
 *
 * @author  <a href="https://github.com/tavonkelly/BungeeServerManager">BungeeServerManager</a> (<a href="https://github.com/tavonkelly">tavonkelly</a>)
 */
@UtilityClass
@SuppressWarnings({"unused", "deprecation"})
public class ServerManager {

    public static ServerInfo getServerInfo(String name) {
        return ProxyServer.getInstance().getServers().get(name);
    }

    public static void addServer(ServerInfo serverInfo) {
        if (isServerExists(serverInfo.getName())) {
            return;
        }

        ProxyServer.getInstance().getServers().put(serverInfo.getName(), serverInfo);
        ProxyServerConfigManager.addToConfig(serverInfo);
    }

    public static void removeServer(String name) {
        if (!isServerExists(name)) {
            return;
        }

        ServerInfo info = ProxyServer.getInstance().getServers().get(name);

        for (ProxiedPlayer p : info.getPlayers()) {
            p.connect(ProxyServer.getInstance().getServers().get(p.getPendingConnection().getListener().getFallbackServer()));
        }

        ProxyServer.getInstance().getServers().remove(name);
        ProxyServerConfigManager.removeFromConfig(name);
    }

    public static int getServerCount() {
        return ProxyServer.getInstance().getServers().size();
    }

    public static boolean isServerExists(String name) {
        return ProxyServer.getInstance().getServers().get(name) != null;
    }

    public static String getServerList() {
        StringBuilder serverList = new StringBuilder();
        for (String server : ProxyServer.getInstance().getServers().keySet()) {
            if (serverList.length() > 0) {
                serverList.append(StringUtils.colorize(("&f")));
                serverList.append(", ");
            }

            serverList.append(StringUtils.colorize("&a"));
            serverList.append(server);
        }

        return serverList.toString();
    }

    public static SocketAddress parseSocketAddress(String input) {
        String[] parts = input.split(":");
        String hostname;
        int port;

        if (parts.length == 1) {
            // Only hostname is provided
            hostname = parts[0].trim();
            port = 25565;
        } else if (parts.length == 2) {
            // Both hostname and port are provided
            hostname = parts[0].trim();
            try {
                port = Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException e) {
                return null; // Invalid port number
            }
        } else {
            // Invalid input format
            return null;
        }

        if (hostname.isEmpty()) {
            hostname = "127.0.0.1"; // Default hostname
        }

        return new InetSocketAddress(hostname, port);
    }

}
