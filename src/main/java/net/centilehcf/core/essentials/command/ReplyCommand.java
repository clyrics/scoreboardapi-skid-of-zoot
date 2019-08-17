package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/5/2019.
 */
@CommandMeta(label = {"reply", "r"})
public class ReplyCommand {
    public void execute(Player player, String message) {
        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (profile.getReplyTo() == null) {
            player.sendMessage(CC.RED + "You have nobody to reply to.");
            return;
        }

        final Player target = Bukkit.getPlayer(profile.getReplyTo());

        if (target == null || !target.isOnline()) {
            player.sendMessage(CC.RED + "That player is no longer online.");
            return;
        }

        Profile targetData = Profile.getByUuid(target.getUniqueId());
        String senderName = CC.RESET + player.getDisplayName();
        String targetName = CC.RESET + target.getDisplayName();

        profile.setReplyTo(target.getUniqueId());
        targetData.setReplyTo(player.getUniqueId());

        String toMessage = CC.GRAY + "(To " + targetName + CC.GRAY + ") " + message;
        String fromMessage = CC.GRAY + "(From " + senderName + CC.GRAY + ") " + message;

        player.sendMessage(toMessage);
        target.sendMessage(fromMessage);
    }
}
