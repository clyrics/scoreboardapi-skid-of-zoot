package net.centilehcf.core.profile.command.individualperms;

import net.centilehcf.core.Locale;
import net.centilehcf.core.Core;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@CommandMeta(label = "addindividualperm", permission = "rank.manager", async = true)
public class AddIndividualPermissionCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile, String permission) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        if (profile.getIndividualPermissions().contains(permission)) {
            sender.sendMessage(CC.RED + profile.getUsername() + " already has the permission " + permission + '!');
            return;
        }

        profile.getIndividualPermissions().add(permission);
        Player player = profile.getPlayer();
        if (player != null) {
            Core.get().getServer().getScheduler().runTask(Core.get(), () -> {
                profile.setupPermissionsAttachment(Core.get(), player);
                sender.sendMessage(CC.YELLOW + "Recalculated permissions for " + player.getName() + '.');
            });
        }
        sender.sendMessage(CC.GREEN + "Added permission " + "\'" + permission + "\' to " + profile.getUsername() + '.');
    }
}

