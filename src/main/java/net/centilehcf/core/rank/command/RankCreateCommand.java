package net.centilehcf.core.rank.command;

import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank create", permission = "rank.manager", async = true)
public class RankCreateCommand {

	public void execute(CommandSender sender, String name) {
		if (Rank.getRankByDisplayName(name) != null) {
			sender.sendMessage(CC.RED + "A rank with that name already exists.");
			return;
		}

		Rank rank = new Rank(name);
		rank.save();

		sender.sendMessage(CC.GREEN + "You created a new rank.");
	}
}
