package net.centilehcf.core.profile.punishment.command;

import net.centilehcf.core.Locale;
import net.centilehcf.core.Core;
import net.centilehcf.core.network.packet.PacketBroadcastPunishment;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.punishment.Punishment;
import net.centilehcf.core.profile.punishment.PunishmentType;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "warn", permission = "rank.trialmod", async = true, options = "s")
public class WarnCommand {

	public void execute(CommandSender sender, CommandOption option, @CPL("player") Profile profile, String reason) {
		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
				.getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

		Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.WARN, System.currentTimeMillis(),
				reason, -1);

		if (sender instanceof Player) {
			punishment.setAddedBy(((Player) sender).getUniqueId());
		}

		profile.getPunishments().add(punishment);
		profile.save();

		Player player = profile.getPlayer();

		if (player != null) {
			String senderName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender).getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";
			player.sendMessage(CC.RED + "You have been warned by " + senderName + CC.RED + " for: " + CC.YELLOW + reason);
		}

		Core.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
				profile.getColoredUsername(), profile.getUuid(), option != null));
	}

}
