package dev.necro.necrocore.managers.servermanager;

import io.netty.channel.unix.DomainSocketAddress;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;
import dev.necro.necrocore.NecroCore;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@UtilityClass
@SuppressWarnings("unused")
public class ProxyServerConfigManager {

    private static File file;
    private static Configuration proxyConfig;
    private static boolean locked; // TODO: This is dumb. Writes are lost when locked

    static {
        // Plugins are loaded before the config (on first run) so uhhhhh, here we go
        setupConfig();

        if (locked) {
            ProxyServer.getInstance().getScheduler().schedule(NecroCore.getInstance(), ProxyServerConfigManager::setupConfig, 5L, TimeUnit.SECONDS);
        }
    }

    public static String socketAddressToString(SocketAddress socketAddress) {
        return socketAddressToString(socketAddress, true);
    }

    public static String socketAddressToString(SocketAddress socketAddress, boolean appendPort) {
        String addressString;

        if (socketAddress instanceof DomainSocketAddress) {
            addressString = "unix:" + ((DomainSocketAddress) socketAddress).path();
        } else if (socketAddress instanceof InetSocketAddress) {
            InetSocketAddress inetAddress = (InetSocketAddress) socketAddress;

            addressString = inetAddress.getHostString();

            if (appendPort) {
                addressString += ":" + inetAddress.getPort();
            }
        } else {
            addressString = socketAddress.toString();
        }

        return addressString;
    }

    public static void addToConfig(ServerInfo serverInfo) {
        if (locked) {
            return;
        }

        proxyConfig.set("servers." + serverInfo.getName() + ".motd", serverInfo.getMotd().replace(ChatColor.COLOR_CHAR, '&'));
        proxyConfig.set("servers." + serverInfo.getName() + ".address", socketAddressToString(serverInfo.getSocketAddress()));
        proxyConfig.set("servers." + serverInfo.getName() + ".restricted", serverInfo.isRestricted());
        saveConfig();
    }

    public static void removeFromConfig(ServerInfo serverInfo) {
        removeFromConfig(serverInfo.getName());
    }

    public static void removeFromConfig(String name) {
        if (locked) {
            return;
        }

        proxyConfig.set("servers." + name, null);
        saveConfig();
    }

    public static void addForcedHost(SocketAddress host, ServerInfo server) {
        changeForcedHost(host, false, server);
    }

    public static void removeForcedHost(SocketAddress host, ServerInfo server) {
        changeForcedHost(host, true, server);
    }

    private static void changeForcedHost(SocketAddress host, boolean remove, ServerInfo server) {
        if (locked) {
            return;
        }

        List<?> listeners = proxyConfig.getList("listeners");
        List<Object> listenersCopy = new ArrayList<>(listeners);

        for (int i = 0; i < listeners.size(); i++) {
            Object listenerObj = listeners.get(i);

            if (!(listenerObj instanceof Map)) {
                continue;
            }

            @SuppressWarnings("unchecked")
            Map<String, Object> listenerMap = (Map<String, Object>) listenerObj;

            Object forcedHostsObj = listenerMap.get("forced_hosts");

            if (!(forcedHostsObj instanceof Map)) {
                continue;
            }

            @SuppressWarnings("unchecked")
            Map<String, String> forcedHostsMap = (Map<String, String>) forcedHostsObj;

            String socketAddressStr = socketAddressToString(host, false);

            if (remove) {
                if (forcedHostsMap.get(socketAddressStr) != null && forcedHostsMap.get(socketAddressStr).equalsIgnoreCase(server.getName())) {
                    forcedHostsMap.remove(socketAddressStr);
                }
            } else {
                forcedHostsMap.put(socketAddressStr, server.getName());
            }

            listenerMap.put("forced_hosts", forcedHostsMap);
            listenersCopy.set(i, listenerMap);
        }

        proxyConfig.set("listeners", listenersCopy);
        saveConfig();
    }

    private static void saveConfig() {
        if (locked) {
            return;
        }

        try {
            YamlConfiguration.getProvider(YamlConfiguration.class).save(proxyConfig, file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void setupConfig() {
        FileInputStream fis = null;
        InputStreamReader isr = null;
        try {
            file = new File(ProxyServer.getInstance().getPluginsFolder().getParentFile(), "config.yml");

            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);

            proxyConfig = YamlConfiguration.getProvider(YamlConfiguration.class).load(isr);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }

                if (isr != null) {
                    isr.close();
                }
            } catch (IOException ignored) {}
        }

        locked = proxyConfig == null;
    }
}
