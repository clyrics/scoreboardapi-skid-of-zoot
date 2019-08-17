package net.centilehcf.core.essentials.command;

import net.centilehcf.core.Core;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "setspawn", permission = "rank.manager")
public class SetSpawnCommand {

	public void execute(Player player) {
		Core.get().getEssentials().setSpawn(player.getLocation());
		player.sendMessage(CC.GREEN + "You updated this world's spawn.");
	}

}
