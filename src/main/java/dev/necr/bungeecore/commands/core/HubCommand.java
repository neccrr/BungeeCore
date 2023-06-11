package dev.necr.bungeecore.commands.core;

import cloud.commandframework.annotations.CommandDescription;
import cloud.commandframework.annotations.CommandMethod;
import dev.necr.bungeecore.commands.api.CommandClass;
import dev.necr.bungeecore.utils.StringUtils;
import dev.necr.bungeecore.utils.Utils;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.List;
import java.util.Random;

@SuppressWarnings("unused")
public class HubCommand extends CommandClass {

    @CommandMethod("hub|lobby")
    @CommandDescription("Sends you to hub servers")
    public void hubCommand(final @NonNull CommandSender sender) {
        if (sender instanceof ProxiedPlayer) {
            if (!Utils.checkPermission(sender, "commands.hub")) {
                return;
            }

            ProxiedPlayer player = (ProxiedPlayer) sender;

            // Randomize the hub servers
            List<String> hubServers = plugin.getMainConfig().HUB_SERVERS();
            Random random = new Random();
            int randomObject = random.nextInt(hubServers.size());
            String result = hubServers.get(randomObject);

            // Checks if the server result is exists on the ProxyServer
            if (ProxyServer.getInstance().getServers().containsKey(result)) {
                // Convert the selected hub server result to ServerInfo
                ServerInfo hubServer = ProxyServer.getInstance().getServerInfo(result);
                // Connect the sender to the selected hub server
                player.connect(hubServer);
            } else {
                int err = 10 + random.nextInt(90);
                player.sendMessage(new TextComponent(StringUtils.colorize(plugin.getMainConfig().PREFIX()) + "&cCan't connect you to Hub servers. If this keeps happening, Please report this to staff members. (0x00" + err + ")"));
            }
        } else {
            sender.sendMessage(new TextComponent(plugin.getMainConfig().PREFIX() + plugin.getMessagesConfig().PLAYER_ONLY()));
        }
    }
}
