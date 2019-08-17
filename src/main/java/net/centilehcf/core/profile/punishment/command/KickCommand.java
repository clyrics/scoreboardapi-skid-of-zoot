package net.centilehcf.core.profile.punishment.command;

import net.centilehcf.core.Locale;
import net.centilehcf.core.Core;
import net.centilehcf.core.network.packet.PacketBroadcastPunishment;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.punishment.Punishment;
import net.centilehcf.core.profile.punishment.PunishmentType;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

@CommandMeta(label = "kick", permission = "rank.trialmod", async = true, options = "s")
public class KickCommand {

	public void execute(CommandSender sender, CommandOption option, Player player, String reason) {
		if (player == null) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		Profile profile = Profile.getProfiles().get(player.getUniqueId());

		if (profile == null || !profile.isLoaded()) {
			sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
			return;
		}

		String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
				.getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

		Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.KICK, System.currentTimeMillis(),
				reason, -1);

		if (sender instanceof Player) {
			punishment.setAddedBy(((Player) sender).getUniqueId());
		}

		profile.getPunishments().add(punishment);
		profile.save();

		Core.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName,
				profile.getColoredUsername(), profile.getUuid(), option != null));

		new BukkitRunnable() {
			@Override
			public void run() {
				player.kickPlayer(punishment.getKickMessage());
			}
		}.runTask(Core.get());
	}

}
