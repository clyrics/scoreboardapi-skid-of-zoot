package net.centilehcf.core.essentials.command;

import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

@CommandMeta(label = { "clearinv", "clear", "ci" }, permission = "rank.admin")
public class ClearCommand {

	public void execute(Player player) {
		player.getInventory().setContents(new ItemStack[36]);
		player.getInventory().setArmorContents(new ItemStack[4]);
		player.updateInventory();
		player.sendMessage(CC.GOLD + "You cleared your inventory.");
	}

	public void execute(CommandSender sender, Player player) {
		player.getInventory().setContents(new ItemStack[36]);
		player.getInventory().setArmorContents(new ItemStack[4]);
		player.updateInventory();
		player.sendMessage(CC.GOLD + "Your inventory has been cleared by " + sender.getName());
	}

}
