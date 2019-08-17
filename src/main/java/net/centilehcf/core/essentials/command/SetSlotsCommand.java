package net.centilehcf.core.essentials.command;

import net.centilehcf.core.Core;
import net.centilehcf.core.util.BukkitReflection;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "setslots", async = true, permission = "rank.owner")
public class SetSlotsCommand {

	public void execute(CommandSender sender, int slots) {
		BukkitReflection.setMaxPlayers(Core.get().getServer(), slots);
		sender.sendMessage(CC.GOLD + "You set the max slots to " + slots + ".");
	}
}
