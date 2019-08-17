package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/4/2019.
 */
@CommandMeta(label = { "teleport", "tp" }, permission = "rank.trialmod")
public class TeleportCommand {
    public void execute(Player player, Player target) {
        if (target == null) {
            player.sendMessage(CC.RED + "This player is not online.");
            return;
        }

        player.teleport(target);
        player.sendMessage(CC.translate(CC.translate("&6Teleported to %TELEPORTER%&6.")).replace("%TELEPORTER%", target.getDisplayName()));
    }
}
