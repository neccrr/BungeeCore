package dev.necro.necrocore;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dev.necro.necrocore.commands.main.NecroCoreCommand;
import dev.necro.necrocore.config.ConfigManager;
import dev.necro.necrocore.config.configs.MainConfigManager;
import dev.necro.necrocore.config.configs.MessagesConfigManager;
import dev.necro.necrocore.dependency.DependencyManager;
import dev.necro.necrocore.managers.ConfirmationManager;
import dev.necro.necrocore.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Getter
public final class NecroCore extends Plugin {

    @Getter
    private static NecroCore instance;

    @Setter
    private ConfigManager mainConfig, messagesConfig;

    private MainConfigManager mainConfigManager;
    private MessagesConfigManager messagesConfigManager;
    private ConfirmationManager confirmationManager;

    private NecroCoreCommand mainCommand;

    /**
     * Plugin startup logic
     */
    @Override
    public void onEnable() {
        long millis = System.currentTimeMillis();

        // Initialize instance variable
        instance = this;

        // Load dependencies
        this.loadDependencies();

        // Hook into LuckPerms
        this.hookLuckPerms();

        // Initialize configs
        this.loadConfigs();

        // Initialize managers
        this.mainConfigManager = new MainConfigManager(this);
        this.messagesConfigManager = new MessagesConfigManager(this);
        this.confirmationManager = new ConfirmationManager(this);

        // Registers commands
        this.mainCommand = new NecroCoreCommand(this);

        // w
        this.sendStuffToConsole();
        this.getLogger().info("NecroCore " + this.getDescription().getVersion() + " loaded in " + (System.currentTimeMillis() - millis) + "ms!");
    }

    /**
     * Plugin shutdown logic
     */
    @Override
    public void onDisable() {
        // Save the configuration files
        this.saveConfigs();

        this.getLogger().info("GoodBye!");
    }

    /**
     * Load/Create configs
     */
    public void loadConfigs() {
        // Initialize config manager
        ConfigManager configManager = new ConfigManager(this);

        // Load main config
        configManager.getConfig("config.yml");
        // Load messages config
        configManager.getConfig("messages.yml");
    }

    /**
     * Save configuration files
     */
    public void saveConfigs() {
        long millis = System.currentTimeMillis();
        this.getLogger().info("Saving configuration files...");

        // Save the main configuration file
        mainConfigManager.save();
        // Save the messages' configuration file
        messagesConfigManager.save();

        this.getLogger().info("Saved the configuration file in " + (System.currentTimeMillis() - millis) + "ms!");
    }

    /**
     * Hooks into LuckPerms
     */
    public void hookLuckPerms() {
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
            this.getLogger().info("Hooked into LuckPerms! (took " + (System.currentTimeMillis() - millis) + "ms)");
        }
    }

    /**
     * Downloads and/or loads dependencies
     */
    private void loadDependencies() {
        long millis = System.currentTimeMillis();

        this.getLogger().info("Loading and injecting dependencies...");
        Map<String, String> dependencyMap = new HashMap<>();

        InputStream stream = null;
        InputStreamReader reader = null;
        try {
            stream = NecroCore.class.getClassLoader().getResourceAsStream("dependencies.json");

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
                if (!dependency.get("name").getAsString().contains("adventure-api")) {
                    dependencyMap.put(
                            dependency.get("name").getAsString(),
                            dependency.get("url").getAsString()
                    );
                }
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

        DependencyManager dependencyManager = new DependencyManager(NecroCore.class.getClassLoader());
        File dir = new File("plugins/NecroCore/libs");
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
     * idk
     */
    private void sendStuffToConsole() {
        String n = StringUtils.colorize(
                "\n" +
                        "&b  _   _                        _____                          \n" +
                        "&b | \\ | |                      / ____|                        \n" +
                        "&b |  \\| | ___  ___ _ __ ___   | |     ___  _ __ ___           \n" +
                        "&b | . ` |/ _ \\/ __| '__/ _ \\  | |    / _ \\| '__/ _ \\       \n" +
                        "&b | |\\  |  __/ (__| | | (_) | | |___| (_) | | |  __/          \n" +
                        "&b |_| \\_|\\___|\\___|_|  \\___/   \\_____\\___/|_|  \\___|    \n" +
                        "                                                                \n" +
                        "    &aNecro Core &ev" + this.getDescription().getVersion() + " &eby &b" + this.getDescription().getAuthor() + "\n" +
                        " ");

        for (String s : n.split("\n")) {
            this.getProxy().getConsole().sendMessage(new TextComponent(s));
        }
    }

}
