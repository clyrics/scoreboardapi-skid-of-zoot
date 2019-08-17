package net.centilehcf.core.essentials.command;

import net.centilehcf.core.prefix.menu.PrefixSelectionMenu;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.prefix.menu.PrefixSelectionMenu;
import org.bukkit.entity.Player;

@CommandMeta(label = "prefix")
public class PrefixCommand {

    public void execute(Player player){
        new PrefixSelectionMenu().openMenu(player);
    }
}
