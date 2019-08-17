package net.centilehcf.core.rank.command;

import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(label = { "rank removepermission", "rank removeperm" }, permission = "rank.manager", async = true)
public class RankRemovePermissionCommand {

	public void execute(CommandSender sender, Rank rank, String permission) {
		if (!rank.hasPermission(permission)) {
			sender.sendMessage(CC.RED + "That rank does not have that permission.");
			return;
		}

		rank.getPermissions().remove(permission);
		rank.save();

		sender.sendMessage(CC.GREEN + "Successfully removed permission from rank.");
	}

}
