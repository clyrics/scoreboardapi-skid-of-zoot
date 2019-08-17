package net.centilehcf.core.profile.command;

import net.centilehcf.core.Core;
import net.centilehcf.core.network.packet.PacketStaffChat;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.entity.Player;

@CommandMeta(label = { "staffchat", "sc" }, permission = "rank.staff")
public class StaffChatCommand {

	public void execute(Player player, String message) {
		Profile profile = Profile.getProfiles().get(player.getUniqueId());

		if (!profile.getStaffOptions().isStaffChatEnabled()) {
			player.sendMessage(CC.RED + "Your staff chat is disabled.");
			return;
		}

		Core.get().getPidgin().sendPacket(new PacketStaffChat(player.getDisplayName(), Core.get().getMainConfig().getString("SERVER_NAME"), message));
	}

}
