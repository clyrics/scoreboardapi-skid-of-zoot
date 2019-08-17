package net.centilehcf.core.profile.punishment.command;

import net.centilehcf.core.Locale;
import net.centilehcf.core.Core;
import net.centilehcf.core.network.packet.PacketBroadcastPunishment;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.punishment.Punishment;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "unmute", permission = "rank.mod", async = true, options = "s")
public class UnmuteCommand {

	public void execute(CommandSender sender, CommandOption option, @CPL("player") Profile profile, String reason) {
		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		if (profile.getActiveMute() == null) {
			sender.sendMessage(CC.RED + "That player is not muted.");
			return;
		}

		String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
				.getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

		Punishment punishment = profile.getActiveMute();
		punishment.setPardonedAt(System.currentTimeMillis());
		punishment.setPardonedReason(reason);
		punishment.setPardoned(true);

		if (sender instanceof Player) {
			punishment.setPardonedBy(((Player) sender).getUniqueId());
		}

		profile.save();

		Core.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
				profile.getColoredUsername(), profile.getUuid(), option != null));
	}

}
