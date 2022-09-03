package dev.necro.necrocore.commands.main;

import cloud.commandframework.Command;
import cloud.commandframework.CommandTree;
import cloud.commandframework.annotations.AnnotationParser;
import cloud.commandframework.arguments.parser.ParserParameters;
import cloud.commandframework.arguments.parser.StandardParameters;
import cloud.commandframework.bungee.BungeeCommandManager;
import cloud.commandframework.execution.CommandExecutionCoordinator;
import cloud.commandframework.meta.CommandMeta;
import cloud.commandframework.minecraft.extras.MinecraftHelp;
import com.google.common.reflect.ClassPath;
import dev.necro.necrocore.NecroCore;
import dev.necro.necrocore.utils.Utils;
import lombok.Getter;
import net.kyori.adventure.platform.bungeecord.BungeeAudiences;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;

import java.io.IOException;
import java.util.Arrays;
import java.util.function.Function;

public class NecroCoreCommand {

    @Getter
    private BungeeCommandManager<CommandSender> commandManager;
    @Getter
    private final NecroCore plugin;
    @Getter
    private final Command.Builder<CommandSender> builder;
    @Getter
    private final MinecraftHelp<CommandSender> minecraftHelp;
    @Getter
    private final AnnotationParser<CommandSender> annotationParser;

    public NecroCoreCommand(NecroCore plugin) {
        this.plugin = plugin;

        Function<CommandTree<CommandSender>, CommandExecutionCoordinator<CommandSender>> executionCoordinatorFunction = CommandExecutionCoordinator.simpleCoordinator();
        Function<CommandSender, CommandSender> mapperFunction = Function.identity();
        try {
            this.commandManager = new BungeeCommandManager<>(
                    plugin,
                    executionCoordinatorFunction,
                    mapperFunction,
                    mapperFunction
            );
        } catch (Exception e) {
            this.plugin.getLogger().severe("Failed to initialize the command Command Manager");
            e.printStackTrace();
        }

        this.builder = this.commandManager.commandBuilder("necrocore", "core", "ncore");

        // registers the custom help command
        BungeeAudiences bungeeAudiences = BungeeAudiences.create(plugin);
        this.minecraftHelp = new MinecraftHelp<>(
                "/necrocore help",
                bungeeAudiences::sender,
                this.commandManager
        );

        // registers the annotation parser
        Function<ParserParameters, CommandMeta> commandMetaFunction = it ->
                CommandMeta.simple()
                        .with(CommandMeta.DESCRIPTION, it.get(StandardParameters.DESCRIPTION, "No description"))
                        .build();

        this.annotationParser = new AnnotationParser<>(
                this.commandManager,
                CommandSender.class,
                commandMetaFunction
        );

        // initializes the default command
        commandManager.command(builder
                .meta(CommandMeta.DESCRIPTION, "The main command")
                .handler(commandContext -> {
                    CommandSender sender = commandContext.getSender();
                    String query = commandContext.getOrDefault("query", null);
                    if (query == null) {
                        sender.sendMessage(new TextComponent(Utils.getPluginDescription()));
                        return;
                    }

                    this.getMinecraftHelp().queryCommands(query, sender);
                })
        );

        this.initCommands();
    }

    @SuppressWarnings("UnstableApiUsage")
    private void initCommands() {
        plugin.getLogger().info("Loading and registering commands...");
        try {
            ClassPath classPath = ClassPath.from(plugin.getClass().getClassLoader());
            for (ClassPath.ClassInfo classInfo : classPath.getTopLevelClassesRecursive("dev.necro.necrocore.commands")) {
                if (classInfo.getName().endsWith("NecroCoreCommand") || classInfo.getName().contains(".api")) {
                    continue;
                }

                try {
                    Class<?> commandClass = Class.forName(classInfo.getName());
                    this.parseAnnotationCommands(commandClass.getDeclaredConstructor().newInstance());
                } catch (Exception e) {
                    plugin.getLogger().severe("Failed loading command class: " + classInfo.getName());
                    e.printStackTrace();
                }
            }

            plugin.getLogger().info("Registered " + commandManager.commands().size() + " commands!");
        } catch (IOException e) {
            plugin.getLogger().severe("Failed loading command classes!");
            e.printStackTrace();
        }
    }

    private void parseAnnotationCommands(Object... clazz) {
        Arrays.stream(clazz).forEach(this.annotationParser::parse);
    }

}
