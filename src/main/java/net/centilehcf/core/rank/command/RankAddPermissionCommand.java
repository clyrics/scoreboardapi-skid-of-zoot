package net.centilehcf.core.rank.command;

import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(label = { "rank addpermission", "rank addperm" }, permission = "rank.manager", async = true)
public class RankAddPermissionCommand {

	public void execute(CommandSender sender, Rank rank, String permission) {
		if (rank.hasPermission(permission)) {
			sender.sendMessage(CC.RED + "That rank already has that permission.");
			return;
		}

		rank.getPermissions().add(permission);
		rank.save();

		sender.sendMessage(CC.GREEN + "Successfully added permission to rank.");
	}

}
