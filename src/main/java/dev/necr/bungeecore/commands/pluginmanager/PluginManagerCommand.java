package dev.necr.bungeecore.commands.pluginmanager;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.specifier.Greedy;
import dev.necr.bungeecore.commands.api.CommandClass;
import dev.necr.bungeecore.utils.Utils;
import dev.necr.bungeecore.managers.pluginmanager.PluginManager;
import dev.necr.bungeecore.managers.pluginmanager.PluginManagerResult;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.io.File;
import java.util.Map;

@SuppressWarnings("unused")
public class PluginManagerCommand extends CommandClass {

    @CommandMethod("bungeecore pluginmanager|pm list")
    @CommandDescription("Lists all loaded plugin")
    public void listCommand(final @NonNull CommandSender sender) {
        if (!Utils.checkPermission(sender, "pluginmanager.list")) {
            return;
        }

        sender.sendMessage(new TextComponent(PluginManager.getPluginList()));
    }

    @CommandMethod("bungeecore pluginmanager|pm load <pluginName>")
    @CommandDescription("Loads a plugin")
    public void loadCommand(final @NonNull CommandSender sender, final @NonNull @Argument(value = "pluginName", description = "The plugin name") @Greedy String pluginName) {
        if (!Utils.checkPermission(sender, "pluginmanager.load")) {
            return;
        }

        String filename = pluginName.replaceAll("[/\\\\]", "");
        File file = new File("plugins", filename + ".jar");

        if (!file.exists()) {
            sender.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().PLUGIN_MANAGER_NOT_FOUND()
                    .replace("{plugin_name}", file.toString())));
        }

        PluginManagerResult pluginResult = PluginManager.loadPlugin(file);
        sender.sendMessage(new TextComponent(pluginResult.getMessage()));
    }

    @CommandMethod("bungeecore pluginmanager|pm unload <pluginName>")
    @CommandDescription("Unloads a plugin")
    public void unloadCommand(final @NonNull CommandSender sender, final @NonNull @Argument(value = "pluginName", description = "The plugin name") @Greedy String pluginName) {
        if (!Utils.checkPermission(sender, "pluginmanager.unload")) {
            return;
        }

        net.md_5.bungee.api.plugin.PluginManager bungeePluginManager = ProxyServer.getInstance().getPluginManager();
        PluginManagerResult pluginResult = PluginManager.unloadPlugin(bungeePluginManager.getPlugin(pluginName));
        sender.sendMessage(new TextComponent(pluginResult.getMessage()));
    }

    @CommandMethod("bungeecore pluginmanager|pm reload <pluginName>")
    @CommandDescription("Reloads a plugin")
    public void reloadCommand(final @NonNull CommandSender sender, final @NonNull @Argument(value = "pluginName", description = "The plugin name") @Greedy String pluginName) {
        if (!Utils.checkPermission(sender, "pluginmanager.reload")) {
            return;
        }

        net.md_5.bungee.api.plugin.PluginManager bungeePluginManager = ProxyServer.getInstance().getPluginManager();
        Map.Entry<PluginManagerResult, PluginManagerResult> pluginResult = PluginManager.reloadPlugin(bungeePluginManager.getPlugin(pluginName));
        sender.sendMessage(new TextComponent(pluginResult.getKey().getMessage()));
        sender.sendMessage(new TextComponent(pluginResult.getValue().getMessage()));
    }
}
