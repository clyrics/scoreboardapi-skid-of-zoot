package net.centilehcf.core.chat.listener;

import net.centilehcf.core.Core;
import net.centilehcf.core.bootstrap.BootstrappedListener;
import net.centilehcf.core.chat.ChatAttempt;
import net.centilehcf.core.chat.event.ChatAttemptEvent;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.Cooldown;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class ChatListener extends BootstrappedListener {

    public ChatListener(Core core) {
        super(core);
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        Profile profile = Profile.getByUuid(player.getUniqueId());

        ChatAttempt chatAttempt = core.getChat().attemptChatMessage(event.getPlayer(), event.getMessage());
        ChatAttemptEvent chatAttemptEvent = new ChatAttemptEvent(event.getPlayer(), chatAttempt, event.getMessage());

        if (!player.hasPermission("core.staff")) {
            if (!profile.getChatCooldown().hasExpired()) {
                player.sendMessage(CC.translate("&cYou can chat again in &c&l%TIME_LEFT%s&c.".replace("%TIME_LEFT%", profile.getChatCooldown().getTimeLeft())));
                event.setCancelled(true);
                return;
            } else {
                profile.setChatCooldown(new Cooldown(Core.get().getChat().getDelayTime() * 1000));
            }
        }

        core.getServer().getPluginManager().callEvent(chatAttemptEvent);

        if (!chatAttemptEvent.isCancelled()) {
            switch (chatAttempt.getResponse()) {
                case ALLOWED: {
                    if (profile.getPrefix() == Core.get().getPrefixHandler().getDefaultPrefix()) {
                        event.setFormat("%1$s" + profile.getActiveGrant().getRank().getSuffix() + CC.RESET + ": %2$s");
                    } else {
                        event.setFormat(profile.getPrefix().getPrefix() + "%1$s" + profile.getActiveGrant().getRank().getSuffix() + CC.RESET + ": %2$s");
                    }
                }
                break;
                case MESSAGE_FILTERED: {
                    event.setCancelled(true);
                    chatAttempt.getFilterFlagged().punish(event.getPlayer());
                }
                break;
                case PLAYER_MUTED: {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(CC.RED + "You are currently muted.");
                    event.getPlayer().sendMessage(CC.RED + "Reason: " + CC.YELLOW + chatAttempt.getPunishment().getAddedReason());
                    event.getPlayer().sendMessage(CC.RED + "Expires: " + CC.YELLOW + chatAttempt.getPunishment().getTimeRemaining());
                }
                break;
                case CHAT_MUTED: {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(CC.RED + "The public chat is currently muted.");
                }
                break;
                case CHAT_DELAYED: {
                    event.setCancelled(true);
                    event.getPlayer().sendMessage(CC.RED + "Slow down! You may chat again in {time}.");
                }
                break;
            }
        }
    }
}
