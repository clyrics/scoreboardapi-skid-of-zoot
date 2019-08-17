package net.centilehcf.core.profile.command.individualperms;

import net.centilehcf.core.Locale;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "listindividualperms", permission = "rank.manager", async = true)
public class ListIndividualPermissionsCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.YELLOW + "Listing individual permissions for " + CC.RED + profile.getUsername() + CC.YELLOW + '.');
        for (String permission : profile.getIndividualPermissions()) {
            sender.sendMessage(CC.RED + permission);
        }
        sender.sendMessage(CC.CHAT_BAR);
    }
}
