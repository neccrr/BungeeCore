package dev.necro.necrocore.managers.pluginmanager;

import dev.necro.necrocore.NecroCore;
import dev.necro.necrocore.utils.StringUtils;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginDescription;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.net.URLClassLoader;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Handler;
import java.util.logging.Level;

/**
 * Utilities for managing plugins.
 *
 * @author  <a href="https://github.com/TheBlackEntity/PlugMan">PlugManX</a> (<a href="https://github.com/TheBlackEntity">TheBlackEntity</a>)
 */
@UtilityClass
public class PluginManager {

    private final NecroCore necroCore = NecroCore.getInstance();

    public static Map.Entry<PluginResult, PluginResult> reloadPlugin(Plugin plugin) {
        File file = plugin.getFile();


        PluginResult result1 = unloadPlugin(plugin);

        PluginResult result2 = loadPlugin(file);

        return new Map.Entry<PluginResult, PluginResult>() {
            @Override
            public PluginResult getKey() {
                return result1;
            }

            @Override
            public PluginResult getValue() {
                return result2;
            }

            @Override
            public PluginResult setValue(PluginResult value) {
                return result2;
            }
        };
    }

    /**
     * Loads and enables a plugin.
     *
     * @param file plugin's name
     */
    public static PluginResult loadPlugin(File file) {
        net.md_5.bungee.api.plugin.PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();

        Field yamlField;
        try {
            yamlField = net.md_5.bungee.api.plugin.PluginManager.class.getDeclaredField("yaml");
            yamlField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return new PluginResult("&cError while trying to load plugin: &4Could not load field 'yaml'&c, see console for more info!", false);
        }

        Yaml yaml;
        try {
            yaml = (Yaml) yamlField.get(pluginManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new PluginResult("&cError while trying to load plugin: &4Could not get field 'yaml'&c, see console for more info!", false);
        }

        Field toLoadField;
        try {
            toLoadField = net.md_5.bungee.api.plugin.PluginManager.class.getDeclaredField("toLoad");
            toLoadField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return new PluginResult("&cError while trying to load plugin: &4Could not load field 'toLoad'&c, see console for more info!", false);
        }

        HashMap<String, PluginDescription> toLoad;
        try {
            toLoad = (HashMap<String, PluginDescription>) toLoadField.get(pluginManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new PluginResult("&cError while trying to load plugin: &4Could not get field 'toLoad'&c, see console for more info!", false);
        }

        if (toLoad == null) {
            toLoad = new HashMap<>();
        }

        if (file.isFile()) {
            PluginDescription desc;

            try (JarFile jar = new JarFile(file)) {
                JarEntry pdf = jar.getJarEntry("bungee.yml");
                if (pdf == null) {
                    pdf = jar.getJarEntry("plugin.yml");
                }

                if (pdf == null) {
                    return new PluginResult("&cError while trying to load plugin: &4Plugin does not contain plugin.yml or bungee.yml!", false);
                }

                // Preconditions.checkNotNull(pdf, "Plugin must have a plugin.yml or bungee.yml");
                try (InputStream in = jar.getInputStream(pdf)) {
                    desc = yaml.loadAs(in, PluginDescription.class);

                    if (desc.getName() == null) {
                        return new PluginResult("&cError while trying to load plugin: &4Plugin does not contain a name in it's plugin.yml/bungee.yml!", false);
                    }

                    if (desc.getMain() == null) {
                        return new PluginResult("&cError while trying to load plugin: &4Plugin does not contain a main class in it's plugin.yml/bungee.yml!", false);
                    }

                    if (pluginManager.getPlugin(desc.getName()) != null) {
                        return new PluginResult("&cError while trying to load plugin: &4A plugin named '" + desc.getName() + "' is already loaded!", false);
                    }

                    desc.setFile(file);

                    toLoad.put(desc.getName(), desc);
                }

                toLoadField.set(pluginManager, toLoad);

                pluginManager.loadPlugins();

                Plugin plugin = pluginManager.getPlugin(desc.getName());
                if (plugin == null)
                    return new PluginResult("&cError while trying to load plugin: &4An unknown error occurred&c, see console for more info!", false);
                plugin.onEnable();
            } catch (Exception ex) {
                ProxyServer.getInstance().getLogger().log(Level.WARNING, "Could not load plugin from file " + file, ex);
                return new PluginResult("&cError while trying to load plugin: &4An unknown error occurred&c, see console for more info!", false);
            }
        }
        return new PluginResult("&7Plugin was loaded successfully!", true);
    }

    /**
     * Disable and unloads a plugin.
     *
     * @param plugin plugin's name
     */
    public static PluginResult unloadPlugin(Plugin plugin) {
        boolean exception = false;
        net.md_5.bungee.api.plugin.PluginManager pluginManager = ProxyServer.getInstance().getPluginManager();
        try {
            plugin.onDisable();
            for (Handler handler : plugin.getLogger().getHandlers()) {
                handler.close();
            }
        } catch (Throwable t) {
            necroCore.getLogger().log(Level.SEVERE, "Exception disabling plugin '" + plugin.getDescription().getName() + "'", t);
            exception = true;
        }

        pluginManager.unregisterCommands(plugin);

        pluginManager.unregisterListeners(plugin);

        ProxyServer.getInstance().getScheduler().cancel(plugin);

        plugin.getExecutorService().shutdownNow();

        Field pluginsField;
        try {
            pluginsField = net.md_5.bungee.api.plugin.PluginManager.class.getDeclaredField("plugins");
            pluginsField.setAccessible(true);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
            return new PluginResult("&cError while trying to unload plugin: &4Could not load field 'plugins'&c, see console for more info!", false);
        }

        Map<String, Plugin> plugins;

        try {
            plugins = (Map<String, Plugin>) pluginsField.get(pluginManager);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return new PluginResult("&cError while trying to unload plugin: &4Could not get field 'plugins'&c, see console for more info!", false);
        }

        plugins.remove(plugin.getDescription().getName());

        ClassLoader cl = plugin.getClass().getClassLoader();

        if (cl instanceof URLClassLoader) {

            try {

                Field pluginField = cl.getClass().getDeclaredField("plugin");
                pluginField.setAccessible(true);
                pluginField.set(cl, null);

                Field pluginInitField = cl.getClass().getDeclaredField("desc");
                pluginInitField.setAccessible(true);
                pluginInitField.set(cl, null);

                Field allLoadersField = cl.getClass().getDeclaredField("allLoaders");
                allLoadersField.setAccessible(true);
                Set allLoaders = (Set) allLoadersField.get(cl);
                allLoaders.remove(cl);

            } catch (NoSuchFieldException | SecurityException | IllegalArgumentException | IllegalAccessException ex) {
                necroCore.getLogger().log(Level.SEVERE, null, ex);

                return new PluginResult("&cError while trying to unload plugin: &4Could not unload ClassLoader&c, see console for more info!", false);
            }

            try {

                ((URLClassLoader) cl).close();
            } catch (IOException ex) {
                necroCore.getLogger().log(Level.SEVERE, null, ex);
                return new PluginResult("&cError while trying to unload plugin: &4Could not close ClassLoader&c, see console for more info!", false);
            }

        }

        // Will not work on processes started with the -XX:+DisableExplicitGC flag, but let's try it anyway.
        // This tries to get around the issue where Windows refuse to unlock jar files that were previously loaded into the JVM.
        System.gc();
        if (exception) {
            return new PluginResult("&cAn unknown error occurred while unloading, see console for more info!", false);
        } else {
            return new PluginResult("&7Plugin was unloaded successfully!", true);
        }
    }

    /**
     * Get list of loaded plugins
     *
     * @return plugin list
     */
    public String getPluginList() {
        StringBuilder pluginList = new StringBuilder();
        Collection<Plugin> plugins = ProxyServer.getInstance().getPluginManager().getPlugins();

        for (Plugin plugin : plugins) {
            if (pluginList.length() > 0) {
                pluginList.append(StringUtils.colorize(("&f")));
                pluginList.append(", ");
            }

            pluginList.append(StringUtils.colorize("&a"));
            pluginList.append(plugin.getDescription().getName());
        }

        return necroCore.getMessagesConfigManager().getPluginList()
                // Placeholders
                .replace("{plugin_amounts}", String.valueOf(plugins.size()))
                .replace("{plugin_lists}", pluginList);
    }
}
