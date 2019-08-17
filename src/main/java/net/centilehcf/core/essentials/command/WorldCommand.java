package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/8/2019.
 */
@CommandMeta(label = "world", permission = "rank.admin")
public class WorldCommand {
    public void execute(Player player, String newworld) {

        World world = Bukkit.getWorld(newworld);
        if (world == null) {
            player.sendMessage(CC.RED + "World not found!");
        }

        if (player.getWorld().equals(world)) {
            player.sendMessage(CC.RED + "You are already in this world!");
        }

        Location playerlocation = player.getLocation();
        Location newlocation = new Location(world, playerlocation.getX(), playerlocation.getY(), playerlocation.getZ(), playerlocation.getYaw(), playerlocation.getPitch());
        player.teleport(newlocation);
        player.sendMessage(CC.GREEN + "You have been sent to world " + CC.PINK + world + CC.GREEN + " !");
    }
}
