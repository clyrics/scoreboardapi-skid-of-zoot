package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.ItemBuilder;
import org.apache.commons.lang.WordUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/12/2019.
 */
@CommandMeta(label = "spawner", permission = "rank.manager")
public class SpawnerCommand {
    public void execute(Player player, Player target, String spawner) {
        Inventory inventory = target.getInventory();
        inventory.addItem(new ItemBuilder(Material.MOB_SPAWNER).name(CC.GREEN + "Spawner").lore(CC.WHITE + WordUtils.capitalizeFully(spawner)).build());
    }
}
