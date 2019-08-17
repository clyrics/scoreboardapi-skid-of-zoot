package net.centilehcf.core.rank.command;

import net.centilehcf.core.Locale;
import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.Locale;
import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank delete", permission = "rank.manager", async = true)
public class RankDeleteCommand {

	public void execute(CommandSender sender, Rank rank) {
		if (rank == null) {
			sender.sendMessage(Locale.RANK_NOT_FOUND.format());
			return;
		}

		rank.delete();

		sender.sendMessage(CC.GREEN + "You deleted the rank.");
	}

}
