package net.centilehcf.core.profile.command;

import net.centilehcf.core.profile.option.menu.ProfileOptionsMenu;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.profile.option.menu.ProfileOptionsMenu;
import org.bukkit.entity.Player;

@CommandMeta(label = "options")
public class OptionsCommand {

	public void execute(Player player) {
		new ProfileOptionsMenu().openMenu(player);
	}

}
