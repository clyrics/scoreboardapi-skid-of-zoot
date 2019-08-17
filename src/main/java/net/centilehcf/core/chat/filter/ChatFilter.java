package net.centilehcf.core.chat.filter;

import net.centilehcf.core.Core;
import net.centilehcf.core.bootstrap.Bootstrapped;
import org.bukkit.entity.Player;

public abstract class ChatFilter extends Bootstrapped {

	private String command;

	public ChatFilter(Core core, String command) {
		super(core);

		this.command = command;
	}

	public abstract boolean isFiltered(String message, String[] words);

	public void punish(Player player) {
		if (command != null) {
			core.getServer().dispatchCommand(core.getServer().getConsoleSender(), command
					.replace("{player}", player.getName())
					.replace("{player-uuid}", player.getUniqueId().toString()));
		}
	}
}
