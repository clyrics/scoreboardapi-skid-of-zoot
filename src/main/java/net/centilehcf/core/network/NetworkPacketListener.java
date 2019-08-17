package net.centilehcf.core.network;

import com.minexd.pidgin.packet.handler.IncomingPacketHandler;
import com.minexd.pidgin.packet.listener.PacketListener;
import net.centilehcf.core.Core;
import net.centilehcf.core.Locale;
import net.centilehcf.core.network.event.ReceiveRequestCommandEvent;
import net.centilehcf.core.network.event.ReceiveStaffChatEvent;
import net.centilehcf.core.network.packet.*;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.grant.Grant;
import net.centilehcf.core.profile.grant.event.GrantAppliedEvent;
import net.centilehcf.core.profile.grant.event.GrantExpireEvent;
import net.centilehcf.core.profile.punishment.Punishment;
import net.centilehcf.core.profile.punishment.PunishmentType;
import net.centilehcf.core.rank.Rank;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Objects;

public class NetworkPacketListener implements PacketListener {

    private Core core;

    public NetworkPacketListener(Core core) {
        this.core = core;
    }

    @IncomingPacketHandler
    public void onAddGrant(PacketAddGrant packet) {
        Player player = Bukkit.getPlayer(packet.getPlayerUuid());
        Grant grant = packet.getGrant();

        if (player != null) {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());
            profile.getGrants().removeIf(other -> Objects.equals(other, grant));
            profile.getGrants().add(grant);

            new GrantAppliedEvent(player, grant);
        }
    }

    @IncomingPacketHandler
    public void onDeleteGrant(PacketDeleteGrant packet) {
        Player player = Bukkit.getPlayer(packet.getPlayerUuid());
        Grant grant = packet.getGrant();

        if (player != null) {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());
            profile.getGrants().removeIf(other -> Objects.equals(other, grant));
            profile.getGrants().add(grant);
            profile.checkGrants();
            new GrantExpireEvent(player, grant);
        }
    }

    @IncomingPacketHandler
    public void onBroadcastPunishment(PacketBroadcastPunishment packet) {
        Punishment punishment = packet.getPunishment();
        punishment.broadcast(packet.getStaff(), packet.getTarget(), packet.isSilent());

        Player player = Bukkit.getPlayer(packet.getTargetUuid());

        if (player != null) {
            Profile profile = Profile.getProfiles().get(player.getUniqueId());
            profile.getPunishments().removeIf(other -> Objects.equals(other, punishment));
            profile.getPunishments().add(punishment);

            if (punishment.getType().isBan()) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.kickPlayer(punishment.getKickMessage());
                    }
                }.runTask(Core.get());
            }

            if (punishment.getType() == PunishmentType.BLACKLIST) {
                new BukkitRunnable() {
                    @Override
                    public void run() {
                        player.kickPlayer(punishment.getKickMessage());
                    }
                }.runTask(Core.get());
            }
        }
    }

    @IncomingPacketHandler
    public void onRankRefresh(PacketRefreshRank packet) {
        Rank rank = Rank.getRankByUuid(packet.getUuid());

        if (rank == null) {
            rank = new Rank(packet.getUuid(), packet.getName());
        }

        rank.load();

        Core.broadcastOps(Locale.NETWORK_RANK_REFRESHED.format(Locale.NETWORK_BROADCAST_PREFIX.format(),
                rank.getDisplayName()));
    }

    @IncomingPacketHandler
    public void onRankDelete(PacketDeleteRank packet) {
        Rank rank = Rank.getRanks().remove(packet.getUuid());

        if (rank != null) {
            Core.broadcastOps(Locale.NETWORK_RANK_DELETED.format(Locale.NETWORK_BROADCAST_PREFIX.format(),
                    rank.getDisplayName()));
        }
    }

    @IncomingPacketHandler
    public void onStaffChat(PacketStaffChat packet) {
        core.getServer().getOnlinePlayers().stream()
                .filter(onlinePlayer -> onlinePlayer.hasPermission("core.staff"))
                .forEach(onlinePlayer -> {
                    ReceiveStaffChatEvent event = new ReceiveStaffChatEvent(onlinePlayer);

                    core.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        if (Profile.getProfiles().get(event.getPlayer().getUniqueId()).getStaffOptions().isStaffChatEnabled()) {
                            onlinePlayer.sendMessage(Locale.STAFF_CHAT.format(Locale.STAFF_BROADCAST_PREFIX.format(),
                                    packet.getPlayerName(), packet.getServerName(), packet.getChatMessage()
                            ));
                        }
                    }
                });
    }

    @IncomingPacketHandler
    public void onRequestCommand(PacketRequestCommand packet) {
        core.getServer().getOnlinePlayers().stream()
                .filter(onlinePlayer -> onlinePlayer.hasPermission("core.staff"))
                .forEach(onlinePlayer -> {
                    ReceiveRequestCommandEvent event = new ReceiveRequestCommandEvent(onlinePlayer);
                    core.getServer().getPluginManager().callEvent(event);

                    if (!event.isCancelled()) {
                        onlinePlayer.sendMessage(Locale.STAFF_REQUEST.format(Locale.STAFF_REQUEST_PREFIX.format(),
                                packet.getServerName(), packet.getPlayerName(), packet.getRequest()
                        ));
                    }
                });
    }

    @IncomingPacketHandler
    public void getServerStatus(PacketServerRestart packet) {
        core.getServer().getOnlinePlayers().stream()
                .filter(onlinePlayer -> onlinePlayer.hasPermission("core.staff"))
                .forEach(onlinePlayer -> onlinePlayer.sendMessage(Locale.SERVER_STATUS.format(Locale.SERVER_STATUS_PREFIX.format(),
                        packet.getServerName(), packet.getStatus())));
    }

    @IncomingPacketHandler
    public void onStaffJoinNetwork(PacketStaffJoinNetwork packet) {
        core.getServer().broadcast(Locale.STAFF_JOIN_NETWORK.format(Locale.STAFF_BROADCAST_PREFIX.format(),
                packet.getPlayerName(), packet.getServerName()), "core.staff");
    }

    @IncomingPacketHandler
    public void onStaffLeaveNetwork(PacketStaffLeaveNetwork packet) {
        core.getServer().broadcast(Locale.STAFF_LEAVE_NETWORK.format(Locale.STAFF_BROADCAST_PREFIX.format(),
                packet.getPlayerName(), packet.getServerName()), "core.staff");
    }

    @IncomingPacketHandler
    public void onStaffSwitchServer(PacketStaffSwitchServer packet) {
        core.getServer().broadcast(Locale.STAFF_SWITCH_SERVER.format(Locale.STAFF_BROADCAST_PREFIX.format(),
                packet.getPlayerName(), packet.getToServerName(), packet.getFromServerName()), "core.staff");
    }

    @IncomingPacketHandler
    public void onPacketRemovePunishments(PacketRemovePunishments packet) {
        Player player = Bukkit.getPlayer(packet.getUuid()); // We don't care if they aren't online
        if (player != null) {
            Profile profile = Profile.getByUuid(packet.getUuid());
            profile.getPunishments().clear();
        }
    }
}
