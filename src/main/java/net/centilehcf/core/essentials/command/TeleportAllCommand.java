package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/4/2019.
 */
@CommandMeta(label = "tpall", permission = "rank.manager")
public class TeleportAllCommand {
    public void execute(Player player) {
        for (Player other : Bukkit.getOnlinePlayers()) {
            if (other == player)
                continue;
            other.teleport(player);
        }
        player.sendMessage(CC.GREEN + "All players have been teleported to you.");
    }
}
