package net.centilehcf.core.profile.command;

import com.qrakn.honcho.command.CPL;
import com.qrakn.honcho.command.CommandMeta;
import net.centilehcf.core.Core;
import net.centilehcf.core.Locale;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.util.CC;
import net.minecraft.util.com.google.common.base.Joiner;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@CommandMeta(label = "alts", async = true, permission = "rank.mod")
public class AltsCommand {

    public void execute(CommandSender sender, @CPL("player") Profile profile) {
        if (profile == null || !profile.isLoaded()) {
            sender.sendMessage(Locale.COULD_NOT_RESOLVE_PLAYER.format());
            return;
        }

        List<String> alts = new ArrayList<>();

        for (UUID altUuid : profile.getKnownAlts()) {
            String cachedName = Core.get().getUuidCache().getName(altUuid);
            if (cachedName != null) {
                alts.add(cachedName);
            }
        }

        if (alts.isEmpty()) {
            sender.sendMessage(CC.RED + "This player has no known alt accounts.");
        } else {
            sender.sendMessage(CC.GOLD + "Alts: " + CC.RESET + Joiner.on(", ").join(alts));
        }
    }

}
