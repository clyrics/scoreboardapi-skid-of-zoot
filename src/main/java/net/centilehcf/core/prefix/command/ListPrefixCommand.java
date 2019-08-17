package net.centilehcf.core.prefix.command;

import net.centilehcf.core.Core;
import net.centilehcf.core.prefix.Prefix;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "prefix list", permission = "rank.manager")
public class ListPrefixCommand {

    public void execute(CommandSender sender) {
        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.YELLOW + "Listing all prefixes");
        for (Prefix prefix : Core.get().getPrefixHandler().getPrefixes()) {
            sender.sendMessage(CC.RED + prefix.getName() + CC.YELLOW + " " + prefix.getPrefixInfo() + CC.GRAY + " (Displays as: " + prefix.getPrefix() + CC.GRAY + ")");
        }
        sender.sendMessage(CC.CHAT_BAR);
    }
}
