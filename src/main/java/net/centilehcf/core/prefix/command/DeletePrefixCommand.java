package net.centilehcf.core.prefix.command;

import net.centilehcf.core.Core;
import net.centilehcf.core.prefix.Prefix;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "prefix delete", permission = "rank.manager")
public class DeletePrefixCommand {

    public void execute(CommandSender sender, Prefix prefix) {
        Core.get().getPrefixHandler().removePrefix(prefix);
        sender.sendMessage(CC.GREEN + "Removed the prefix \'" + prefix.getName() + "\'.");
    }
}
