package dev.necro.necrocore.managers;

import dev.necro.necrocore.NecroCore;
import dev.necro.necrocore.callbacks.CanSkipCallback;
import dev.necro.necrocore.utils.StringUtils;
import lombok.RequiredArgsConstructor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
public class ConfirmationManager {

    private final NecroCore plugin;
    private final Map<ProxiedPlayer, Callable> confirmationMap = new HashMap<>();

    /**
     * Requests a confirmation before executing something
     *
     * @param callable the code to execute if confirmed
     * @param sender   the executor
     * @param skip     the required condition to skip the confirmation
     * @param warnings warnings to be sent to the player
     */
    public void requestConfirmation(Callable callable, CommandSender sender, boolean skip, @Nullable List<String> warnings) {
        if (!skip || sender instanceof ProxiedPlayer) {
            ProxiedPlayer player = (ProxiedPlayer) sender;
            if (warnings != null && !warnings.isEmpty()) {
                warnings.forEach(player::sendMessage);
            }

            player.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + StringUtils.colorize("&ePlease type &b/necrocore confirm &eto confirm your action.")));
            this.confirmationMap.put(player, callable);
        } else {
            callable.call();
        }
    }

    /**
     * @see ConfirmationManager#requestConfirmation(Callable, CommandSender, boolean, List)
     */
    public void requestConfirmation(Callable callable, CommandSender sender, boolean skip) {
        this.requestConfirmation(callable, sender, skip, null);
    }

    /**
     * @see ConfirmationManager#requestConfirmation(Callable, CommandSender, boolean, List)
     */
    public void requestConfirmation(Callable callable, CommandSender sender, @Nullable List<String> warnings) {
        this.requestConfirmation(callable, sender, false, warnings);
    }

    /**
     * @see ConfirmationManager#requestConfirmation(Callable, CommandSender, boolean, List)
     */
    public void requestConfirmation(Callable callable, CommandSender sender) {
        this.requestConfirmation(callable, sender, false, null);
    }

    /**
     * @see ConfirmationManager#requestConfirmation(Callable, CommandSender, boolean, List)
     */
    public void requestConfirmation(Callable callable, CanSkipCallback skipCallback) {
        this.requestConfirmation(callable, skipCallback.getSender(), skipCallback.isCanSkip(), skipCallback.getReason());
    }

    /**
     * Confirms the execution of a pending code
     *
     * @param player the player
     */
    public void confirm(ProxiedPlayer player) {
        if (!this.confirmationMap.containsKey(player)) {
            player.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + StringUtils.colorize("&cYou don't have any pending action!")));
            return;
        }

        player.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + StringUtils.colorize("&aAction confirmed.")));
        this.confirmationMap.get(player).call();
    }


    /**
     * Deletes the queued pending code from {@link ConfirmationManager#confirmationMap}
     *
     * @param player the player who has the code pending
     */
    public void deleteConfirmation(ProxiedPlayer player) {
        if (!this.confirmationMap.containsKey(player)) {
            player.sendMessage(new TextComponent(plugin.getMainConfig().getPREFIX() + StringUtils.colorize("&cYou don't have any pending action!")));
            return;
        }

        this.confirmationMap.remove(player);
    }


    public interface Callable {
        void call();
    }
}
