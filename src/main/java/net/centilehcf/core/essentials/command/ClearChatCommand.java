package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.Core;
import net.centilehcf.core.util.CC;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/12/2019.
 */
@CommandMeta(label = {"clearchat", "cc", "removeallshitinthechat"}, permission = "rank.mod")
public class ClearChatCommand {
    public void execute(Player player) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < 100; i++) {
            builder.append("§a §b §c §d §e §f §0 §r \n");
        }

        final String clear = builder.toString();

        for (Player player2 : Core.get().getServer().getOnlinePlayers()) {
            if (!player2.hasPermission("plib.staff")) {
                player2.sendMessage(clear);
            }

            player2.sendMessage(CC.GREEN + "Chat has been cleared!");
        }
    }
}
