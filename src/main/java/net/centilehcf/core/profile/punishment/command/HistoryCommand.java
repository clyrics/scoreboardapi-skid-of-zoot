package net.centilehcf.core.profile.punishment.command;

import net.centilehcf.core.Locale;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.punishment.menu.PunishmentsMenu;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = { "history", "punishments" }, permission = "rank.trialmod", async = true)
public class HistoryCommand {

	public void execute(Player player, @CPL("player") Profile profile) {
		if (profile == null || !profile.isLoaded()) {
			player.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		new PunishmentsMenu(profile).openMenu(player);
	}

}
