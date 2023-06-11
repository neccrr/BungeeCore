package dev.necr.bungeecore.commands.main;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import dev.necr.bungeecore.commands.api.CommandClass;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

@SuppressWarnings("unused")
public class ConfirmCommand extends CommandClass {

    @CommandMethod("bungeecore confirm|yes|accept")
    @CommandDescription("Confirms a pending command action")
    public void confirmCommand (final @NonNull ProxiedPlayer sender) {
        plugin.getConfirmationManager().confirm(sender);
    }

    @CommandMethod("bungeecore cancel|undo|deny|no")
    @CommandDescription("Cancels a pending command action")
    public void cancelCommand (final @NonNull ProxiedPlayer sender) {
        plugin.getConfirmationManager().deleteConfirmation(sender);
    }

}
