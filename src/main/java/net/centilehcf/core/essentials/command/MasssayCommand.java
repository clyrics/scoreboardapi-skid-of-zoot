package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 3/30/2019.
 */
@CommandMeta(label = "masssay", permission = "rank.manager")
public class MasssayCommand {

    public void execute(Player player, String message) {
        message = CC.translate(message); // translate the colors
        for (Player players : Bukkit.getOnlinePlayers()) {
            players.chat(message);
        }
        player.sendMessage(CC.GREEN + "Just made all players type '" + message + CC.GREEN + "'");
    }
}
