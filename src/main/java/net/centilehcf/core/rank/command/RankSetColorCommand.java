package net.centilehcf.core.rank.command;

import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank setcolor", permission = "rank.manager", async = true)
public class RankSetColorCommand {

	public void execute(CommandSender sender, Rank rank, String color) {
		if (rank == null) {
			sender.sendMessage(CC.RED + "A rank with that name does not exist.");
			return;
		}

		try {
			rank.setColor(ChatColor.valueOf(color));
			rank.save();
		} catch (Exception e) {
			e.printStackTrace();
			sender.sendMessage(CC.RED + "That color is not valid.");
			return;
		}

		sender.sendMessage(CC.GREEN + "You updated the rank's color.");
	}

}
