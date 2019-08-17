package net.centilehcf.core.essentials.command;

import net.centilehcf.core.Core;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "spawn", permission = "rank.trialmod")
public class SpawnCommand {

	public void execute(Player player) {
		Core.get().getEssentials().teleportToSpawn(player);
		player.sendMessage(CC.GREEN + "You teleported to this world's spawn.");
	}
}
