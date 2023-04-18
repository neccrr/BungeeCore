package dev.necro.necrocore.commands.main;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import dev.necro.necrocore.commands.api.CommandClass;
import dev.necro.necrocore.utils.StringUtils;
import dev.necro.necrocore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

@SuppressWarnings("unused")
public class InfoCommand extends CommandClass {

    @CommandMethod("necrocore info|ver")
    @CommandDescription("Information about the plugin")
    public void infoCommand(final @NonNull CommandSender sender) {
        sender.sendMessage(new TextComponent(Utils.getPluginDescription()));
    }

}
