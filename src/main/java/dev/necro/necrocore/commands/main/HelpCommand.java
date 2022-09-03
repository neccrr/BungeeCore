package dev.necro.necrocore.commands.main;

import cloud.commandframework.annotations.Argument;
import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.specifier.Greedy;
import dev.necro.necrocore.commands.api.CommandClass;
import net.md_5.bungee.api.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public class HelpCommand extends CommandClass {

    @CommandMethod("necrocore help|? [query]")
    @CommandDescription("Help menu for Necro Core")
    public void helpCommand(final @NonNull CommandSender sender, final @Nullable @Argument(value = "query", description = "The subcommand or the help page") @Greedy String query) {
        plugin.getMainCommand().getMinecraftHelp().queryCommands(query == null ? "" : query, sender);
    }
}
