package net.centilehcf.core.rank.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.Locale;
import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.util.CC;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "rank rename", permission = "rank.owner", async = true)
public class RankRenameCommand {

    public void execute(CommandSender sender, @CPL(value = "rank") Rank rank, @CPL(value = "newName") String newName) {
        if (rank == null){
            sender.sendMessage(CC.RED + Locale.RANK_NOT_FOUND);
            return;
        }

        rank.setDisplayName(newName);
        rank.save();
        sender.sendMessage(CC.GREEN + "Set rank name to " + newName + '.');
    }
}
