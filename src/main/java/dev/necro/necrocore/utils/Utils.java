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
        silent = plugin.getMainConfig().isSilentPermsCheck();

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

    /**
     * Obtain target's client version from their protocol number
     *
     * @return Target's client version
     */
    public static String getClientVersion(int targetProtocolNumber) {

        if (targetProtocolNumber > 760) {
            return "SNAPSHOT || UNKNOWN";
        } else if (targetProtocolNumber >= 760) {
            return "1.19.1";
        } else if (targetProtocolNumber >= 759) {
            return "1.19";
        } else if (targetProtocolNumber >= 758) {
            return "1.18.2";
        } else if (targetProtocolNumber >= 757) {
            return "1.18.1 || 1.18";
        } else if (targetProtocolNumber >= 756) {
            return "1.17.1";
        } else if (targetProtocolNumber >= 755) {
            return "1.17";
        } else if (targetProtocolNumber >= 754) {
            return "1.16.5 || 1.16.4";
        } else if (targetProtocolNumber >= 753) {
            return "1.16.3";
        } else if (targetProtocolNumber >= 752) {
            return "1.16.3-rc1";
        } else if (targetProtocolNumber >= 751) {
            return "1.16.2";
        } else if (targetProtocolNumber >= 750) {
            return "1.16.2-rc2";
        } else if (targetProtocolNumber >= 749) {
            return "1.16.2-rc1";
        } else if (targetProtocolNumber >= 736) {
            return "1.16.1";
        } else if (targetProtocolNumber >= 735) {
            return "1.16";
        } else if (targetProtocolNumber >= 734) {
            return "1.16-rc1";
        } else if (targetProtocolNumber >= 578) {
            return "1.15.2";
        } else if (targetProtocolNumber >= 575) {
            return "1.15.1";
        } else if (targetProtocolNumber >= 573) {
            return "1.15";
        } else if (targetProtocolNumber >= 498) {
            return "1.14.4";
        } else if (targetProtocolNumber >= 490) {
            return "1.14.3";
        } else if (targetProtocolNumber >= 485) {
            return "1.14.2";
        } else if (targetProtocolNumber >= 480) {
            return "1.14.1";
        } else if (targetProtocolNumber >= 477) {
            return "1.14";
        } else if (targetProtocolNumber >= 404) {
            return  "1.13.2";
        } else if (targetProtocolNumber >= 401) {
            return  "1.13.1";
        } else if (targetProtocolNumber >= 393) {
            return  "1.13";
        } else if (targetProtocolNumber >= 340) {
            return "1.12.2";
        } else if (targetProtocolNumber >= 338) {
            return  "1.12.1";
        } else if (targetProtocolNumber >= 335) {
            return  "1.12";
        } else if (targetProtocolNumber >= 316) {
            return "1.11.2 || 1.11.1 || 16w50a";
        } else if (targetProtocolNumber >= 315) {
            return "1.11";
        } else if (targetProtocolNumber >= 210) {
            return "1.10.2 || 1.10.1 || 1.10";
        } else if (targetProtocolNumber >= 110) {
            return "1.9.4 || 1.9.3 || 1.9.3-pre3 || 1.9.3-pre2";
        } else if (targetProtocolNumber >= 109) {
            return "1.9.1 || 1.9.1-pre3 || 1.9.1-pre2";
        } else if (targetProtocolNumber >= 107) {
            return "1.9 || 1.9.1-pre1";
        } else if (targetProtocolNumber >= 47) {
            return "1.8.X";
        } else {
            return targetProtocolNumber >= 4 ? "1.7.X" : "UNKNOWN";
        }
    }
}
