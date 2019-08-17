package net.centilehcf.core.prefix.command;

import net.centilehcf.core.prefix.Prefix;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.util.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "prefix setprefix", permission = "rank.manager")
public class SetPrefixWeightCommand {

    public void execute(CommandSender sender, Prefix prefix, int weight) {
        prefix.setWeight(weight);
        sender.sendMessage(CC.GREEN + "Set the weight of \'" + prefix.getName() + "\' to " + weight + '.');
    }
}
