package net.centilehcf.core.rank.command;

import net.centilehcf.core.rank.Rank;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.rank.comparator.RankComparator;
import net.centilehcf.core.util.CC;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@CommandMeta(label = "rank list", permission = "rank.manager", async = true)
public class RankListCommand {

    public void execute(CommandSender sender) {
        List<Rank> ranks = Rank.getRanks().values().stream().sorted(new RankComparator()).collect(Collectors.toList());
        sender.sendMessage(CC.CHAT_BAR);
        sender.sendMessage(CC.YELLOW + "Listing all ranks...");
        for (Rank rank : ranks) {
            sender.sendMessage(rank.getDisplayName() + (rank.isDefaultRank() ? " (Default)" : "") +
                    " (Weight: " + rank.getWeight() + ")");
        }
        sender.sendMessage(CC.CHAT_BAR);
    }
}