package dev.necr.bungeecore.hooks;

import dev.necr.bungeecore.BungeeCore;
import dev.necr.bungeecore.utils.StringUtils;
import lombok.experimental.UtilityClass;
import net.luckperms.api.cacheddata.CachedMetaData;
import net.luckperms.api.model.group.Group;
import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Utilities for getting data from LuckPerms
 */
@UtilityClass
public class LuckPermsHook {

    private final BungeeCore plugin = BungeeCore.getInstance();

    /**
     * Get player's LuckPerms prefix
     *
     * @param player The player's name
     *
     * @return Colorized player's LuckPerms prefix
     */
    public String getPrefix(ProxiedPlayer player) {
        CachedMetaData playerMetaData = plugin.getLuckPerms().getPlayerAdapter(ProxiedPlayer.class).getMetaData(player);
        String prefix = playerMetaData.getPrefix();
        if (prefix == null) {
            return StringUtils.colorize("&7");
        }

        return StringUtils.colorize(prefix);
    }

    /**
     * Get player's LuckPerms suffix
     *
     * @param player The player's name
     *
     * @return Colorized player's LuckPerms suffix
     */
    public String getSuffix(ProxiedPlayer player) {
        CachedMetaData playerMetaData = plugin.getLuckPerms().getPlayerAdapter(ProxiedPlayer.class).getMetaData(player);
        String suffix = playerMetaData.getSuffix();
        if (suffix == null) {
            return StringUtils.colorize("&7");
        }

        return StringUtils.colorize(suffix);
    }

    /**
     * Get group's LuckPerms display name
     *
     * @param player The player's name, I use player's name to obtain the group
     *
     * @return Colorized group's LuckPerms display name
     */
    public String getGroupDisplayName(ProxiedPlayer player) {
        CachedMetaData playerMetaData = plugin.getLuckPerms().getPlayerAdapter(ProxiedPlayer.class).getMetaData(player);
        String playerGroup = (playerMetaData.getPrimaryGroup() != null) ? playerMetaData.getPrimaryGroup() : "default";

        Group group = plugin.getLuckPerms().getGroupManager().getGroup(playerGroup);
        assert group != null;
        String groupDisplayName = group.getFriendlyName();

        return StringUtils.colorize(groupDisplayName);
    }
}
