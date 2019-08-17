package net.centilehcf.core.rank.command;

import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank setsuffix", permission = "rank.manager", async = true)
public class RankSetSuffixCommand {

	public void execute(CommandSender sender, Rank rank, String suffix) {
		if (rank == null) {
			sender.sendMessage(CC.RED + "A rank with that name does not exist.");
			return;
		}

		rank.setSuffix(CC.translate(suffix));
		rank.save();

		sender.sendMessage(CC.GREEN + "You updated the rank's suffix.");
	}

}
