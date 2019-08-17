package net.centilehcf.core.chat.command;

import net.centilehcf.core.Core;
import net.centilehcf.core.util.CC;
import com.qrakn.honcho.command.CommandMeta;
import org.bukkit.command.CommandSender;

@CommandMeta(label = "mutechat", permission = "core.seniormod")
public class MuteChatCommand {

    public void execute(CommandSender sender) {
        Core.get().getChat().setPublicChatMuted(!Core.get().getChat().isPublicChatMuted());
        Core.get().getServer().broadcastMessage((CC.PINK + "Public chat has been {context} by " + sender.getName())
                .replace("{context}", Core.get().getChat().isPublicChatMuted() ? "muted" : "unmuted"));
    }
}
