package net.centilehcf.core.profile.grant.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.Core;
import net.centilehcf.core.Locale;
import net.centilehcf.core.network.packet.PacketClearGrants;
import net.centilehcf.core.profile.Profile;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 5/14/2019.
 */
@CommandMeta(label = "cleargrants", permission = "rank.manager", async = true)
public class ClearGrantsCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile) {
        if (profile == null) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        profile.getGrants().clear();
        profile.save();

        Core.get().getPidgin().sendPacket(new PacketClearGrants(profile.getUuid()));

        sender.sendMessage(ChatColor.GREEN + "Cleared grants of " + profile.getPlayer().getName() + "!");
    }

}
