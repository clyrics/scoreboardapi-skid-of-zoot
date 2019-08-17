package net.centilehcf.core.profile.punishment.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import com.qrakn.honcho.command.CommandOption;
import net.centilehcf.core.Core;
import net.centilehcf.core.Locale;
import net.centilehcf.core.network.packet.PacketBroadcastPunishment;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.punishment.Punishment;
import net.centilehcf.core.util.CC;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/21/2019.
 */
@CommandMeta(label = "unblacklist", permission = "rank.owner", async = true, options = "s")
public class UnblacklistCommand {

    public void execute(CommandSender sender, CommandOption option, @CPL("player") Profile profile, String reason) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getActiveBlacklist() == null) {
            sender.sendMessage(CC.RED + "That player is not blacklisted!");
            return;
        }

        String staffName = sender instanceof Player ? Profile.getProfiles().get(((Player) sender).getUniqueId()).getColoredUsername() : CC.DARK_RED + "Console";

        Punishment punishment = profile.getActiveBlacklist();
        punishment.setPardonedAt(System.currentTimeMillis());
        punishment.setPardonedReason(reason);
        punishment.setPardoned(true);

        if (sender instanceof Player) {
            punishment.setPardonedBy(((Player) sender).getUniqueId());
        }

        profile.save();

        Core.get().getPidgin().sendPacket(new PacketBroadcastPunishment(punishment, staffName, profile.getColoredUsername(), profile.getUuid(), option != null));
    }
}
