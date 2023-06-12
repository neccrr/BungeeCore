package dev.necr.bungeecore;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.necr.bungeecore.commands.main.BungeeCoreCommand;
import dev.necr.bungeecore.configuration.ConfigManager;
import dev.necr.bungeecore.configuration.configs.MessagesConfig;
import dev.necr.bungeecore.dependency.DependencyManager;
import dev.necr.bungeecore.listeners.CommandBlockerListener;
import dev.necr.bungeecore.managers.ConfirmationManager;
import dev.necr.bungeecore.utils.StringUtils;
import dev.necr.bungeecore.configuration.configs.MainConfig;
import lombok.Getter;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Getter
@SuppressWarnings("ResultOfMethodCallIgnored")
public final class BungeeCore extends Plugin {

    @Getter
    private static BungeeCore instance;

    private ConfigManager configManager;
    private MainConfig mainConfig;
    private MessagesConfig messagesConfig;

    private ConfirmationManager confirmationManager;

    private BungeeCoreCommand mainCommand;

    private LuckPerms luckPerms;

    /**
     * Plugin startup logic
     */
    @Override
    public void onEnable() {
        long millis = System.currentTimeMillis();

        // Load dependencies
        this.loadDependencies();

        // Initialize plugin instance
        instance = this;

        // Hook into LuckPerms
        this.hookLuckPerms();

        // Initialize configs
        this.loadConfigs();

        // Initialize managers
        this.confirmationManager = new ConfirmationManager(this);

        // Registers commands
        this.mainCommand = new BungeeCoreCommand(this);

        // Registers listeners
        this.registerListeners();

        this.startupMessage();
        this.getLogger().info("BungeeCore " + this.getDescription().getVersion() + " loaded in " + (System.currentTimeMillis() - millis) + "ms!");
    }

    /**
     * Plugin shutdown logic
     */
    @Override
    public void onDisable() {
        this.getLogger().info("Starting shutdown process...");

        this.getLogger().info("GoodBye!");
    }

    /**
     * Downloads and/or loads dependencies
     */
    private void loadDependencies() {
        long millis = System.currentTimeMillis();

        this.getLogger().info("Loading and injecting dependencies...");
        this.getLogger().info("(If this is the first time it may take a while to download all the dependencies)");
        Map<String, String> dependencyMap = new HashMap<>();

        InputStream stream = null;
        InputStreamReader reader = null;
        try {
            stream = BungeeCore.class.getClassLoader().getResourceAsStream("dependencies.json");

            assert stream != null;
            reader = new InputStreamReader(stream);

            /* Deprecated method
            JsonParser parser = new JsonParser();
            JsonArray dependencies = parser.parse(reader).getAsJsonArray();
            */
            JsonArray dependencies = JsonParser.parseReader(reader).getAsJsonArray();

            if (dependencies.size() == 0) {
                return;
            }

            for (JsonElement element : dependencies) {
                JsonObject dependency = element.getAsJsonObject();
                dependencyMap.put(
                        dependency.get("name").getAsString(),
                        dependency.get("url").getAsString()
                );
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        DependencyManager dependencyManager = new DependencyManager(BungeeCore.class.getClassLoader());
        File dir = new File("plugins/BungeeCore/libs");
        if (!dir.exists()) {
            dir.mkdir();
        }
        try {
            dependencyManager.download(dependencyMap, dir.toPath());
            dependencyManager.loadDir(dir.toPath());
        } catch (IOException | IllegalAccessException e) {
            e.printStackTrace();
        }

        this.getLogger().info("Loaded/injected and/or downloaded all dependencies in " + (System.currentTimeMillis() - millis) + "ms!");
    }

    /**
     * Hooks into LuckPerms
     */
    private void hookLuckPerms() {
        long millis = System.currentTimeMillis();
        this.getLogger().info("Hooking into LuckPerms...");
        if (this.getProxy().getPluginManager().getPlugin("LuckPerms") == null) {
            this.getLogger().info("");
            this.getLogger().severe("WARNING! Unable to hook into LuckPerms!");
            this.getLogger().severe("Make sure you installed LuckPerms correctly!");
            this.getLogger().severe("This plugin won't work properly without LuckPerms!");
            this.getLogger().severe("https://luckperms.net/download");
            this.getLogger().info("");
        } else {
            // Initialize the LuckPerms API using LuckPermsProvider
            luckPerms = LuckPermsProvider.get();
            this.getLogger().info("Hooked into LuckPerms! (took " + (System.currentTimeMillis() - millis) + "ms)");
        }
    }

    /**
     * Load/Create configs
     */
    private void loadConfigs() {
        // Initialize config manager
        long millis = System.currentTimeMillis();
        this.getLogger().info("Loading configurations...");

        this.configManager = new ConfigManager(this);

        try {
            this.mainConfig = new MainConfig();
            this.messagesConfig = new MessagesConfig();
        } catch (IOException e) {
            this.getLogger().severe("Cannot load the Configuration!");
            e.printStackTrace();
        }

        this.getLogger().info("Configurations loaded in " + (System.currentTimeMillis() - millis) + "ms!");
    }

    /**
     * Register Listeners
     */
    private void registerListeners() {
        this.getProxy().getPluginManager().registerListener(this, new CommandBlockerListener(this));
    }

    /**
     * idk
     */
    private void startupMessage() {
        String message = StringUtils.colorize(
                "\n" +
                        "&b  ____                                 _____                        \n" +
                        "&b |  _ \\                               / ____|                      \n" +
                        "&b | |_) |_   _ _ __   __ _  ___  ___  | |     ___  _ __ ___          \n" +
                        "&b |  _ <| | | | '_ \\ / _` |/ _ \\/ _ \\ | |    / _ \\| '__/ _ \\    \n" +
                        "&b | |_) | |_| | | | | (_| |  __/  __/ | |___| (_) | | |  __/         \n" +
                        "&b |____/ \\__,_|_| |_|\\__, |\\___|\\___|  \\_____\\___/|_|  \\___|  \n" +
                        "&b                     __/ |                                          \n" +
                        "&b                    |___/                                           \n" +
                        "                                                                \n" +
                        "    &bBungee Core &ev" + this.getDescription().getVersion() + " &eby &b" + this.getDescription().getAuthor() + "\n" +
                        "    &bRunning on " + ProxyServer.getInstance().getName() + "\n" +
                        " ");

        for (String str : message.split("\n")) {
            this.getProxy().getConsole().sendMessage(new TextComponent(str));
        }
    }

}
