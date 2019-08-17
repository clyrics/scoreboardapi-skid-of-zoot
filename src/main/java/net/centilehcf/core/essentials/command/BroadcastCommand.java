package net.centilehcf.core.essentials.command;

import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

@CommandMeta(label = { "broadcast", "bc" }, options = "r", permission = "rank.manager")
public class BroadcastCommand {

	public void execute(CommandSender sender, CommandOption option, String broadcast) {
		String message = broadcast.replaceAll("(&([a-f0-9l-or]))", "\u00A7$2");
		Bukkit.broadcastMessage(CC.translate((option == null ? "&6[Broadcast] &r" : "") + message));
	}
}
