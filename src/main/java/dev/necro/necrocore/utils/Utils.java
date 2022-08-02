package dev.necro.necrocore.utils;

import dev.necro.necrocore.NecroCore;
import lombok.experimental.UtilityClass;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import org.jetbrains.annotations.Nullable;

@UtilityClass
public class Utils {

    private final NecroCore plugin;
    private String pluginDescription = null;

    static {
        plugin = NecroCore.getInstance();
    }

    /**
     * Gets the plugin description
     */
    public String getPluginDescription() {
        if (pluginDescription == null) {
            pluginDescription = plugin.getMainConfig().getPrefix() + StringUtils.colorize("&eThis server is running &bNecroCore &b" + plugin.getDescription().getVersion() + " &eby &b" + plugin.getDescription().getAuthor());
        }

        return pluginDescription;
    }

    /**
     * see {@link Utils#checkPermission(CommandSender, String, boolean, boolean, boolean, String)}
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean checkPermission(CommandSender sender, String permission) {
        return Utils.checkPermission(sender, permission, false, false, false, null);
    }

    /**
     * see {@link Utils#checkPermission(CommandSender, String, boolean, boolean, boolean, String)}
     */
    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean checkPermission(CommandSender sender, boolean others, String permission) {
        return Utils.checkPermission(sender, permission, others, false, false, null);
    }

    /**
     * checks if the command target has a certain permission
     *
     * @param target         the target
     * @param permission     the permission
     * @param others         is the target executing this to other player(s)?
     * @param showPermission should the permission deny message show the required permission?
     * @param silent         should the target be notified of their lack of permission?
     * @param command        the command, if set to null then the permission deny message will show the executed command.
     * @return see {@link CommandSender#hasPermission(String)}
     */
    public boolean checkPermission(CommandSender target, String permission, boolean others, boolean showPermission, boolean silent, @Nullable String command) {
        permission = "necrocore." + permission.toLowerCase();
        silent = plugin.getMainConfig().isSilent();

        if (others) {
            permission += ".others";
        }

        if (target.hasPermission(permission)) {
            return true;
        }

        if (!silent) {
            if (showPermission) {
                if (command != null) {
                    target.sendMessage(new TextComponent(plugin.getMainConfig().getPrefix() +  StringUtils.colorize("&cYou don't have the required permission &l" + permission + " to do &l" + command + "&c!")));
                } else {
                    target.sendMessage(new TextComponent(plugin.getMainConfig().getPrefix() +  StringUtils.colorize("&cYou don't have the required permission &l" + permission + " to do that!")));
                }
            } else {
                if (command != null) {
                    target.sendMessage(new TextComponent(plugin.getMainConfig().getPrefix() +  StringUtils.colorize("&cYou don't have the required permission to do &l" + command + "&c!")));
                } else {
                    target.sendMessage(new TextComponent(plugin.getMainConfig().getPrefix() +  StringUtils.colorize("&cYou don't have the required permission to do that!")));
                }
            }
        } else {
            target.sendMessage(new TextComponent(StringUtils.colorize("Unknown command! Type \"/help\" for help!")));
        }
        return false;
    }
}
