package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 5/28/2019.
 */
@CommandMeta(label = "rename", permission = "command.rename")
public class RenameCommand {

    public static final List<String> DENIED_NAMES = Arrays.asList("nigger", "n1gger");

    public void execute(Player player, String newName) {
        ItemStack stack = player.getItemInHand();
        if (stack == null || stack.getType() == Material.AIR) {
            player.sendMessage(CC.RED + "You have nothing in your hand.");
            return;
        }

        ItemMeta meta = stack.getItemMeta();
        String oldname = meta.getDisplayName();

        if (oldname != null) {
            oldname = oldname.trim();
        }

        if (oldname == null && newName == null) {
            player.sendMessage(CC.RED + "Item already has no name.");
            return;
        }

        if (newName != null) {
            for (String word : DENIED_NAMES) {
                if (newName.toLowerCase().contains(word)) {
                    player.sendMessage(CC.RED + "This name is not allowed.");
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "warn -s " + player + " Disallowed Rename Name.");
                    return;
                }
            }
        }

        meta.setDisplayName(CC.translate(newName));
        stack.setItemMeta(meta);
        player.sendMessage(CC.translate("&aItem has been renamed from &c" + oldname + " &ato &c" + newName + "&a."));
    }
}
