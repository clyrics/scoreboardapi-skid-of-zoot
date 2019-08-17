package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/8/2019.
 */
@CommandMeta(label = "streaming", permission = "rank.media")
public class StreamingCommand {
    public void execute(Player player, String url) {
       Bukkit.broadcastMessage(player.getDisplayName() + CC.YELLOW + " is currently streaming! " + CC.GRAY + "(" +  url + ")");
    }
}
