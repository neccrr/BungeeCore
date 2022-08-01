package dev.necro.necrocore.commands.pluginmanager;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.specifier.Greedy;
import dev.necro.necrocore.commands.api.CommandClass;
import dev.necro.necrocore.managers.pluginmanager.PluginManager;
import dev.necro.necrocore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;

public class PluginManagerCommand extends CommandClass {

    @CommandMethod("necrocore pluginmanager|pm list")
    @CommandDescription("Lists all loaded plugin")
    public void listCommand(final @NonNull CommandSender sender) {
        if (!Utils.checkPermission(sender, "pluginmanager.list")) {
            return;
        }

        sender.sendMessage(new TextComponent(PluginManager.getPluginList()));
    }

    @CommandMethod("necrocore pluginmanager|pm load <pluginName>")
    @CommandDescription("Loads a plugin")
    public void loadCommand(final @NonNull CommandSender sender, final @NonNull @Argument(value = "pluginName", description = "The plugin name") @Greedy String pluginName) {
        if (!Utils.checkPermission(sender, "pluginmanager.load")) {
            return;
        }

        String filename = pluginName.replaceAll("[/\\\\]", "");
        File file = new File("plugins", filename + ".jar");

        if (!file.exists()) {
            sender.sendMessage(new TextComponent(plugin.getMessagesConfigManager().getPluginNotFound()
                    .replace("{plugin_name}", file.toString())));
        }

        PluginManager.loadPlugin(file);
    }

    @CommandMethod("necrocore pluginmanager|pm unload <pluginName>")
    @CommandDescription("Unloads a plugin")
    public void unloadCommand(final @NonNull CommandSender sender, final @NonNull @Argument(value = "pluginName", description = "The plugin name") @Greedy String pluginName) {
        if (!Utils.checkPermission(sender, "pluginmanager.unload")) {
            return;
        }

        net.md_5.bungee.api.plugin.PluginManager bungeePluginManager = ProxyServer.getInstance().getPluginManager();
        PluginManager.unloadPlugin(bungeePluginManager.getPlugin(pluginName));
    }

    @CommandMethod("necrocore pluginmanager|pm reload <pluginName>")
    @CommandDescription("Reloads a plugin")
    public void reloadCommand(final @NonNull CommandSender sender, final @NonNull @Argument(value = "pluginName", description = "The plugin name") @Greedy String pluginName) {
        if (!Utils.checkPermission(sender, "pluginmanager.reload")) {
            return;
        }

        net.md_5.bungee.api.plugin.PluginManager bungeePluginManager = ProxyServer.getInstance().getPluginManager();
        PluginManager.reloadPlugin(bungeePluginManager.getPlugin(pluginName));
    }
}
