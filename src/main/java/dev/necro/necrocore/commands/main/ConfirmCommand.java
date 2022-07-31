package dev.necro.necrocore.commands.main;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import cloud.commandframework.annotations.ProxiedBy;
import dev.necro.necrocore.commands.api.CommandClass;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

public class ConfirmCommand extends CommandClass {

    @ProxiedBy("confirm")
    @CommandMethod("necrocore confirm|yes|accept")
    @CommandDescription("Confirms a pending command action")
    public void confirmCommand (
            final @NonNull ProxiedPlayer sender
    ) {
        plugin.getConfirmationManager().confirm(sender);
    }

    @ProxiedBy("cancel")
    @CommandMethod("necrocore cancel|undo|deny|no")
    @CommandDescription("Cancels a pending command action")
    public void cancelCommand (
            final @NonNull ProxiedPlayer sender
    ) {
        plugin.getConfirmationManager().deleteConfirmation(sender);
    }

}
