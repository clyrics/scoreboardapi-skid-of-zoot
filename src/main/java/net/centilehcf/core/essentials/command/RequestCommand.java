package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.Core;
import net.centilehcf.core.network.packet.PacketRequestCommand;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.Cooldown;
import org.bukkit.entity.Player;

import java.util.concurrent.TimeUnit;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/21/2019.
 */
@CommandMeta(label = {"request", "helpop"})
public class RequestCommand {

    private static final long requestcooldown = TimeUnit.SECONDS.toMillis(60);

    public void execute(Player player, String request) {
        Profile profile = Profile.getByUuid(player.getUniqueId());

        if (!profile.getRequestCooldown().hasExpired()) {
            player.sendMessage(CC.RED + "You must wait before you can request assistance again.");
            return;
        }

        player.sendMessage(CC.GREEN + "We have received your request and will help soon. Please be patient.");
        profile.setRequestCooldown(new Cooldown(requestcooldown));
        Core.get().getPidgin().sendPacket(new PacketRequestCommand(Core.get().getMainConfig().getString("SERVER_NAME"), player.getDisplayName(), request));
    }
}
