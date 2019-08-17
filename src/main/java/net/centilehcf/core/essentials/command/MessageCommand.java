package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.util.CC;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/5/2019.
 */
@CommandMeta(label = {"message", "msg", "m", "tell", "whisper"})
public class MessageCommand {
    public void execute(Player player, Player target, String message) {
        if (target == null) {
            player.sendMessage(CC.RED + "This player is not online.");
            return;
        }

        Profile targetprofile = Profile.getByUuid(target.getUniqueId());
        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (!targetprofile.getOptions().isPrivateChatEnabled()) {
            player.sendMessage(CC.RED + "This player has messages disabled.");
            return;
        }

        if (!profile.getOptions().isPrivateChatEnabled()) {
            player.sendMessage(CC.RED + "You have messages disabled.");
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
