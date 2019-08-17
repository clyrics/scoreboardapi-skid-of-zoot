package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 5/28/2019.
 */
@CommandMeta(label = "craft", permission = "command.craft")
public class CraftCommand {

    public void execute(Player player) {
        player.openWorkbench(player.getLocation(), true);
    }
}

