package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/6/2019.
 */
@CommandMeta(label = "ping")
public class PingCommand {
    public void execute(Player player, Player target) {

        if (target == null) {
            player.sendMessage(CC.RED + "Player isn't online!");
            return;
        }

        int ping = ((CraftPlayer) player).getHandle().ping;
        player.sendMessage(target.getDisplayName() + CC.YELLOW + "'s ping: " + (ping > 100 ? CC.RED : (ping > 50 ? CC.YELLOW : CC.GREEN)) + ping);
    }
}
