package net.centilehcf.core.profile.grant.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.Core;
import net.centilehcf.core.Locale;
import net.centilehcf.core.network.packet.PacketAddGrant;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.grant.Grant;
import net.centilehcf.core.profile.grant.event.GrantAppliedEvent;
import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.TimeUtil;
import net.centilehcf.core.util.duration.Duration;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Date;
import java.util.UUID;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 7/20/2019.
 */
@CommandMeta(label = "setrank", async = true, permission = "rank.manager")
public class SetRankCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile, Rank rank, Duration duration, String reason) {
        if (rank == null) {
            sender.sendMessage(Locale.RANK_NOT_FOUND.format());
            return;
        }

        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (duration.getValue() == -1) {
            sender.sendMessage(CC.RED + "That duration is not valid.");
            sender.sendMessage(CC.RED + "Example: [perm/1y1m1w1d]");
            return;
        }

        UUID addedBy = sender instanceof Player ? ((Player) sender).getUniqueId() : null;
        Grant grant = new Grant(UUID.randomUUID(), rank, addedBy, System.currentTimeMillis(), reason,
                duration.getValue());

        profile.getGrants().add(grant);
        profile.save();
        profile.activateNextGrant();

        Core.get().getPidgin().sendPacket(new PacketAddGrant(profile.getUuid(), grant));

        sender.sendMessage(CC.GREEN + "You applied a `{rank}` grant to `{player}` for {time-remaining}."
                .replace("{rank}", rank.getDisplayName())
                .replace("{player}", profile.getUsername())
                .replace("{time-remaining}", duration.getValue() == Integer.MAX_VALUE ? "forever"
                        : TimeUtil.dateToString(new Date(System.currentTimeMillis() + duration.getValue()))));

        Player player = profile.getPlayer();

        if (player != null) {
            new GrantAppliedEvent(player, grant).call();
        }
    }
}