package net.centilehcf.core.profile.grant.command;

import net.centilehcf.core.Locale;
import net.centilehcf.core.Core;
import net.centilehcf.core.network.packet.PacketAddGrant;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.grant.Grant;
import net.centilehcf.core.profile.grant.event.GrantAppliedEvent;
import net.centilehcf.core.profile.grant.menu.RankSelectionMenu;
import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.TimeUtil;
import net.centilehcf.core.util.duration.Duration;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;

import java.util.Date;
import java.util.UUID;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "grant", async = true, permission = "rank.manager")
public class GrantCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile) {

        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }
        new RankSelectionMenu(profile).openMenu((Player) sender);
    }
}
