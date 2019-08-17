package net.centilehcf.core.profile.punishment.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import net.centilehcf.core.Core;
import net.centilehcf.core.Locale;
import net.centilehcf.core.network.packet.PacketBroadcastPunishment;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.punishment.Punishment;
import net.centilehcf.core.profile.punishment.PunishmentType;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.duration.Duration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.UUID;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/21/2019.
 */
@CommandMeta(label = "blacklist", permission = "rank.owner", async = true, options = "s")
public class BlacklistCommand {

    public void execute(CommandSender sender, CommandOption option, @CPL("player") Profile profile, String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getActiveBlacklist() != null) {
            sender.sendMessage(CC.RED + "That player is already blacklisted!");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender)
                .getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = new Punishment(UUID.randomUUID(), PunishmentType.BLACKLIST, System.currentTimeMillis(), reason, Integer.MAX_VALUE);

        if (sender instanceof Player) {
            punishment.setAddedBy(((Player) sender).getUniqueId());
        }

        profile.getPunishments().add(punishment);
        profile.save();

        Core.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName, profile.getColoredUsername(), profile.getUuid(), option != null));

        Player player = profile.getPlayer();

        if (player != null) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    player.kickPlayer(punishment.getKickMessage());
                }
            }.runTask(Core.get());
        }
    }
}

