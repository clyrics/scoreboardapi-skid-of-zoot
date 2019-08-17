package net.centilehcf.core.profile;

import net.centilehcf.core.Locale;
import net.centilehcf.core.Core;
import net.centilehcf.core.bootstrap.BootstrappedListener;
import net.centilehcf.core.network.packet.PacketStaffJoinNetwork;
import net.centilehcf.core.network.packet.PacketStaffLeaveNetwork;
import net.centilehcf.core.prefix.Prefix;
import net.centilehcf.core.prefix.PrefixHandler;
import net.centilehcf.core.profile.punishment.Punishment;
import net.centilehcf.core.util.CC;

import java.util.List;
import java.util.logging.Level;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ProfileListener extends BootstrappedListener {

    public ProfileListener(Core core) {
        super(core);
    }

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent event) {
        Player player = core.getServer().getPlayer(event.getUniqueId());

        if (!Core.get().isLoaded()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(CC.RED + "The server is starting...");
            return;
        }
        // Need to check if player is still logged in when receiving another login attempt
        // This happens when a player using a custom client can access the server list while in-game (and reconnecting)
        if (player != null && player.isOnline()) {
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            event.setKickMessage(CC.RED + "You tried to login too quickly after disconnecting.\nTry again in a few seconds.");
            core.getServer().getScheduler().runTask(core, () -> player.kickPlayer(CC.RED + "Duplicate login kick"));
            return;
        }

        Profile profile = null;

        try {
            profile = new Profile(event.getName(), event.getUniqueId());

            if (!profile.isLoaded()) {
                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.setKickMessage(Locale.FAILED_TO_LOAD_PROFILE.format());
                return;
            }

            if (profile.getActiveBan() != null) {
                handleBan(event, profile.getActiveBan());
                return;
            }

            if (profile.getActiveBlacklist() != null) {
                handleBan(event, profile.getActiveBlacklist());
            }

            profile.setUsername(event.getName());

            if (profile.getFirstSeen() == null) {
                profile.setFirstSeen(System.currentTimeMillis());
            }

            profile.setLastSeen(System.currentTimeMillis());

            if (profile.getCurrentAddress() == null) {
                profile.setCurrentAddress(event.getAddress().getHostAddress());
            }

            if (!profile.getIpAddresses().contains(event.getAddress().getHostAddress())) {
                profile.getIpAddresses().add(event.getAddress().getHostAddress());
            }

            if (!profile.getCurrentAddress().equals(event.getAddress().getHostAddress())) {
                List<Profile> alts = Profile.getByIpAddress(event.getAddress().getHostAddress());

                for (Profile alt : alts) {
                    if (alt.getActiveBan() != null) {
                        handleBan(event, alt.getActiveBan());
                        return;
                    }
                    profile.getKnownAlts().add(alt.getUuid());
                }
            }

            profile.save();
        } catch (Exception e) {
            core.debug(Level.SEVERE, "Failed to load profile for " + event.getName(), e);
        }

        if (profile == null || !profile.isLoaded()) {
            event.setKickMessage(Locale.FAILED_TO_LOAD_PROFILE.format());
            event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
            return;
        }

        Profile.getProfiles().put(profile.getUuid(), profile);

        core.getUuidCache().update(event.getName(), event.getUniqueId());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player player = event.getPlayer();
        Profile profile = Profile.getProfiles().get(player.getUniqueId());
        profile.setupPermissionsAttachment(core, event.getPlayer());

        player.setDisplayName(profile.getActiveGrant().getRank().getPrefix() + player.getName() + profile.getActiveGrant().getRank().getSuffix());

        if (player.hasPermission("core.staff")) {
            String server = Core.get().getMainConfig().getConfiguration().getString("SERVER_NAME");
            Core.get().getPidgin().sendPacket(new PacketStaffJoinNetwork(player.getDisplayName(), server));
        }
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
        Profile profile = Profile.getProfiles().remove(event.getPlayer().getUniqueId());
        profile.setLastSeen(System.currentTimeMillis());
        Player player = event.getPlayer();

        if (player.hasPermission("core.staff")) {
            String server = Core.get().getMainConfig().getConfiguration().getString("SERVER_NAME");
            Core.get().getPidgin().sendPacket(new PacketStaffLeaveNetwork(player.getDisplayName(), server));
        }

        if (profile.isLoaded()) {
            new BukkitRunnable() {
                @Override
                public void run() {
                    try {
                        profile.save();
                    } catch (Exception e) {
                        core.debug(Level.SEVERE, "Failed to save profile " + event.getPlayer().getName(), e);
                    }
                }
            }.runTaskAsynchronously(Core.get());
        }
    }

    private void handleBan(AsyncPlayerPreLoginEvent event, Punishment punishment) {
        event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        event.setKickMessage(punishment.getKickMessage());
    }
}
