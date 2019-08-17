package net.centilehcf.core.profile.grant.command;

import net.centilehcf.core.Locale;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.grant.menu.GrantsMenu;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = "grants", async = true, permission = "rank.manager")
public class GrantsCommand {

	public void execute(Player player, @CPL("player") Profile profile) {
		if (profile == null || !profile.isLoaded()) {
			player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		new GrantsMenu(profile).openMenu(player);
	}
}
