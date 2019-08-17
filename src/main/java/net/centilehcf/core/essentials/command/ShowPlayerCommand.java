package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "showplayer", permission = "rank.manager")
public class ShowPlayerCommand {

	public void execute(Player player, Player target) {
		player.showPlayer(target);
	}
}
