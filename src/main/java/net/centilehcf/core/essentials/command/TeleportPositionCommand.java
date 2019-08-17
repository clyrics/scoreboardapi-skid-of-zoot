package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/4/2019.
 */
@CommandMeta(label = "tppos", permission = "rank.trialmod")
public class TeleportPositionCommand {

    public void execute(Player player, Double x, Double y, Double z) {
        Location location = new Location(player.getWorld(), x, y, z);

        player.teleport(location);
        player.sendMessage(CC.translate(CC.translate("&6Teleported to x:&f %X% &6y: &f%Y%&6 z:&f%Z%")).replace("%X%", "" + x).replace("%Y%", "" + y).replace("%Z%", "" + z));
    }
}
