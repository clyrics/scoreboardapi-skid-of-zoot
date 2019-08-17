package net.centilehcf.core.essentials.command;

import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.rank.comparator.RankComparator;
import net.centilehcf.core.util.CC;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.libs.joptsimple.internal.Strings;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@CommandMeta(label = {"list", "who", "online"}, async = true)
public class ListCommand {

    public void execute(CommandSender sender) {
        List<Rank> ranks = Rank.getRanks().values().stream().sorted(new RankComparator()).collect(Collectors.toList());
        List<String> rankStr = ranks.stream().filter(rank -> !rank.getPermissions().contains("hidden")).map(rank -> rank.getColor() + rank.getDisplayName()).collect(Collectors.toList());
        sender.sendMessage(Strings.join(rankStr, CC.GRAY + ", "));
        List<Player> onlinePlayers = Bukkit.getOnlinePlayers().stream().sorted(Comparator.comparingInt(player -> Profile.getByUuid(player.getUniqueId()).getActiveRank().getWeight())).collect(Collectors.toList());
        Collections.reverse(onlinePlayers);
        String onlinePlayersOutOfMaximumPlayers = CC.WHITE + "(" + Bukkit.getOnlinePlayers().size() + "/" + Bukkit.getMaxPlayers() + ") ";
        if (sender.hasPermission("core.staff")) {
            List<String> list = new ArrayList<>();
            for (Player player : onlinePlayers) {
                String vanished = (player.hasMetadata("VANISHED") ? CC.GRAY + "*" : "") + Profile.getByUuid(player.getUniqueId()).getColoredUsername();
                list.add(vanished);
            }
            sender.sendMessage(onlinePlayersOutOfMaximumPlayers + Strings.join(list, CC.GRAY + ", "));
        } else {
            List<String> list = new ArrayList<>();
            for (Player player : onlinePlayers) {
                if (!player.hasMetadata("VANISHED")) {
                    String coloredUsername = Profile.getByUuid(player.getUniqueId()).getColoredUsername();
                    list.add(coloredUsername);
                }
            }
            sender.sendMessage(onlinePlayersOutOfMaximumPlayers + Strings.join(list, CC.GRAY + ", "));
        }
    }
}
