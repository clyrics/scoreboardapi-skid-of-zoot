package net.centilehcf.core.profile.grant.listener;

import net.centilehcf.core.Core;
import net.centilehcf.core.bootstrap.BootstrappedListener;
import net.centilehcf.core.network.packet.PacketDeleteGrant;
import net.centilehcf.core.profile.grant.Grant;
import net.centilehcf.core.profile.grant.event.GrantAppliedEvent;
import net.centilehcf.core.profile.grant.event.GrantExpireEvent;
import net.centilehcf.core.profile.grant.procedure.GrantProcedure;
import net.centilehcf.core.profile.grant.procedure.GrantProcedureStage;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.TimeUtil;
import net.centilehcf.core.util.callback.TypeCallback;
import net.centilehcf.core.util.menu.menus.ConfirmMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Date;

public class GrantListener extends BootstrappedListener {

    public GrantListener(Core core) {
        super(core);
    }

    @EventHandler
    public void onGrantAppliedEvent(GrantAppliedEvent event) {
        Player player = event.getPlayer();
        Grant grant = event.getGrant();

        player.sendMessage(CC.GREEN + ("A `{rank}` grant has been applied to you for {time-remaining}.")
                .replace("{rank}", grant.getRank().getDisplayName())
                .replace("{time-remaining}", grant.getDuration() == Integer.MAX_VALUE ?
                        "forever" : TimeUtil.millisToRoundedTime(
                        System.currentTimeMillis() - (grant.getAddedAt() + grant.getDuration()))));
    }

    @EventHandler
    public void onGrantExpireEvent(GrantExpireEvent event) {
        Player player = event.getPlayer();
        Grant grant = event.getGrant();

        player.sendMessage(CC.RED + ("Your `{rank}` grant has expired.")
                .replace("{rank}", grant.getRank().getDisplayName())
                .replace("", ""));
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
        if (!event.getPlayer().hasPermission("rank.manager")) {
            return;
        }

        GrantProcedure procedure = GrantProcedure.getByPlayer(event.getPlayer());

        if (procedure != null && procedure.getStage() == GrantProcedureStage.REQUIRE_TEXT) {
            event.setCancelled(true);

            if (event.getMessage().equalsIgnoreCase("cancel")) {
                GrantProcedure.getProcedures().remove(procedure);
                event.getPlayer().sendMessage(CC.RED + "You have cancelled the grant procedure.");
                return;
            }

            switch (procedure.getType()) {
                // TODO: 5/28/2019 Finish
                /*case GRANT: {

                    new ConfirmMenu(CC.YELLOW + "Add this grant?", (TypeCallback<Boolean>) data -> {
                        if (data) {
                            procedure.finish();
                            event.getPlayer().sendMessage(CC.GREEN + "You applied a `{rank}` grant to `{player}` for {time-remaining}."
                                    .replace("{rank}", procedure.getGrant().getRank().getDisplayName())
                                    .replace("{player}", procedure.getRecipient().getUsername())
                                    .replace("{time-remaining}", procedure.getGrant().getDuration() == Integer.MAX_VALUE ? "forever"
                                            : TimeUtil.dateToString(new Date(System.currentTimeMillis() + procedure.getGrant().getDuration()))));
                        } else {
                            procedure.cancel();
                            event.getPlayer().sendMessage(CC.RED + "You have cancelled the grant procedure.");
                        }
                    }, true) {
                        @Override
                        public void onClose(Player player) {
                            if (!isClosedByMenu()) {
                                procedure.cancel();
                                event.getPlayer().sendMessage(CC.RED + "You have cancelled the grant procedure.");
                            }
                        }
                    }.openMenu(event.getPlayer());
                }*/
                case REMOVE: {
                    new ConfirmMenu(CC.YELLOW + "Delete this grant?", (TypeCallback<Boolean>) data -> {
                        if (data) {
                            procedure.getGrant().setRemovedBy(event.getPlayer().getUniqueId());
                            procedure.getGrant().setRemovedAt(System.currentTimeMillis());
                            procedure.getGrant().setRemovedReason(event.getMessage());
                            procedure.getGrant().setRemoved(true);
                            procedure.finish();
                            event.getPlayer().sendMessage(CC.GREEN + "The grant has been removed.");

                            Core.get().getPidgin().sendPacket(new PacketDeleteGrant(procedure.getRecipient().getUuid(), procedure.getGrant()));
                        } else {
                            procedure.cancel();
                            event.getPlayer().sendMessage(CC.RED + "You did not confirm to remove the grant.");
                        }
                    }, true) {
                        @Override
                        public void onClose(Player player) {
                            if (!isClosedByMenu()) {
                                procedure.cancel();
                                event.getPlayer().sendMessage(CC.RED + "You did not confirm to remove the grant.");
                            }
                        }
                    }.openMenu(event.getPlayer());
                }
            }
        }
    }

}
