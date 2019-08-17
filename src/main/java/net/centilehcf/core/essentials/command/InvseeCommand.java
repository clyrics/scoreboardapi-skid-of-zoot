package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/5/2019.
 */
@CommandMeta(label = "invsee", permission = "rank.mod")
public class InvseeCommand {
    public void execute(Player player, Player target) {
        player.openInventory(target.getInventory());
    }
}
