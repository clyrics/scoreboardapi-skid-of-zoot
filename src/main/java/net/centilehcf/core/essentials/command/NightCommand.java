package net.centilehcf.core.essentials.command;

import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "night")
public class NightCommand {

	public void execute(Player player) {
		player.setPlayerTime(18000L, false);
		player.sendMessage(CC.GREEN + "It's now night time.");
	}

}
