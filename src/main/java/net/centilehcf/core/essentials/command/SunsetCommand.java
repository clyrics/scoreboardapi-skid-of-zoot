package net.centilehcf.core.essentials.command;

import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(label = "sunset")
public class SunsetCommand {

	public void execute(Player player) {
		player.setPlayerTime(12000, false);
		player.sendMessage(CC.GREEN + "It's now sunset.");
	}

}
