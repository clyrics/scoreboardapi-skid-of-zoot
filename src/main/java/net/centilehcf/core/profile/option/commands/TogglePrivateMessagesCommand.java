package net.centilehcf.core.profile.option.commands;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.Locale;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.util.CC;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 5/28/2019.
 */
@CommandMeta(label = {"togglepm", "togglepms", "tpm", "tpms"})
public class TogglePrivateMessagesCommand {
    public void execute(Player player) {
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.getOptions().setPrivateChatEnabled(!profile.getOptions().isPrivateChatEnabled());

        player.sendMessage(profile.getOptions().isPrivateChatEnabled() ? CC.GREEN + "You have enabled private messages." : CC.RED + "You have disabled private messages.");
    }
}
