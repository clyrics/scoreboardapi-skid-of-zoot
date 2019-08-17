package net.centilehcf.core.rank.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.command.CommandSender;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 3/30/2019.
 */
@CommandMeta(label = {"rank"}, permission = "rank.manager", async = true)
public class RankCommand {
    public void execute(CommandSender sender) {
        sender.sendMessage(CC.DARK_RED + "Rank Commands");
        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.DARK_RED + "/rank addpermission <rank> <permission> - Adds a permission to a Rank!");
        sender.sendMessage(CC.RED + "Usage: /rank convert <pex|plib|other>");
        sender.sendMessage(CC.RED + "Usage: /rank convert <pex|plib|other>");
        sender.sendMessage(CC.RED + "Usage: /rank convert <pex|plib|other>");
        sender.sendMessage(CC.RED + "Usage: /rank convert <pex|plib|other>");
        sender.sendMessage(CC.RED + "Usage: /rank convert <pex|plib|other>");
        sender.sendMessage(CC.RED + "Usage: /rank convert <pex|plib|other>");
        sender.sendMessage(CC.RED + "Usage: /rank convert <pex|plib|other>");
        sender.sendMessage(CC.RED + "Usage: /rank convert <pex|plib|other>");
        sender.sendMessage(CC.RED + "Usage: /rank convert <pex|plib|other>");
    }
}
