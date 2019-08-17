package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/4/2019.
 */
@CommandMeta(label = "tphere", permission = "rank.seniormod")
public class TeleportHereCommand {

    public void execute(Player player, Player target) {
        if (target == null) {
            player.sendMessage(CC.RED + "This player is not online.");
            return;
        }

        target.teleport(player);
        player.sendMessage(CC.translate(CC.translate("&6Teleported %PLAYER% &6to %TELEPORTER%&6.")).replace("%PLAYER%", target.getDisplayName()).replace("%TELEPORTER%", player.getDisplayName()));
    }
}
