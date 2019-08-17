package net.centilehcf.core.prefix.command;

import net.centilehcf.core.Core;
import net.centilehcf.core.prefix.Prefix;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "prefix add", permission = "rank.manager")
public class AddPrefixCommand {

    public void execute(CommandSender sender, String name){
        if (Core.get().getPrefixHandler().getPrefixByName(name) != null){
            sender.sendMessage(CC.RED + "A prefix with the name " + "\'" + name + "\' already exists!");
            return;
        }

        Prefix prefix = new Prefix(name);
        Core.get().getPrefixHandler().savePrefix(prefix);
        sender.sendMessage(CC.GREEN + "Created a new prefix with the name \'" + name + "\'.");
    }
}
