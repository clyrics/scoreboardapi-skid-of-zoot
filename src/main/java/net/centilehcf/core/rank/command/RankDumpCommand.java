package net.centilehcf.core.rank.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/5/2019.
 */
@CommandMeta(label = "rank dump", permission = "rank.manager", async = true)
public class RankDumpCommand {
    public void execute(Player player, Rank rank) {
        player.sendMessage(rank.getDisplayName() + "'s Permissions:");

        for (String permission : rank.getAllPermissions()) {
            player.sendMessage(CC.GRAY + " * " + permission);
        }
    }
}
