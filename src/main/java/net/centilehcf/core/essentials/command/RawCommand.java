package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import net.centilehcf.core.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 3/30/2019.
 */
@CommandMeta(label = "raw", options = "r", permission = "rank.manager", async = true)
public class RawCommand {
    public void execute(CommandSender sender, CommandOption option, String broadcast) {
        String message = broadcast.replaceAll("(&([a-f0-9l-or]))", "\u00A7$2");
        Bukkit.broadcastMessage(CC.translate((option == null ? "" : "") + message));
    }
}
