package dev.necr.bungeecore.commands.main;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import dev.necr.bungeecore.commands.api.CommandClass;
import dev.necr.bungeecore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

@SuppressWarnings("unused")
public class InfoCommand extends CommandClass {

    @CommandMethod("bungeecore info|ver")
    @CommandDescription("Information about the plugin")
    public void infoCommand(final @NonNull CommandSender sender) {
        sender.sendMessage(new TextComponent(Utils.getPluginDescription()));
    }

}
