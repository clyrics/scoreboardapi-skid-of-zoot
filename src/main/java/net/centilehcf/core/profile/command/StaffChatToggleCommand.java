package net.centilehcf.core.profile.command;

import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.entity.Player;

@CommandMeta(label = "toggle", permission = "rank.staff")
public class StaffChatToggleCommand {

	public void execute(Player player) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());
		profile.getStaffOptions().setStaffChatEnabled(!profile.getStaffOptions().isStaffChatEnabled());

		player.sendMessage(profile.getStaffOptions().isStaffChatEnabled() ? CC.GREEN + "You have enabled your staff chat." : CC.RED + "You have disabled your staff chat.");
	}
}
