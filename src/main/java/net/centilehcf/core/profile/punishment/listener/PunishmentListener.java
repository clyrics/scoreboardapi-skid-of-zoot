package net.centilehcf.core.profile.punishment.listener;

import net.centilehcf.core.Core;
import net.centilehcf.core.bootstrap.BootstrappedListener;
import net.centilehcf.core.profile.punishment.procedure.PunishmentProcedure;
import net.centilehcf.core.profile.punishment.procedure.PunishmentProcedureStage;
import net.centilehcf.core.profile.punishment.procedure.PunishmentProcedureType;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.callback.TypeCallback;
import net.centilehcf.core.util.menu.menus.ConfirmMenu;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class PunishmentListener extends BootstrappedListener {

	public PunishmentListener(Core core) {
		super(core);
	}

	@EventHandler(priority = EventPriority.LOWEST)
	public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event) {
		if (!event.getPlayer().hasPermission("rank.manager")) {
			return;
		}

		PunishmentProcedure procedure = PunishmentProcedure.getByPlayer(event.getPlayer());

		if (procedure != null && procedure.getStage() == PunishmentProcedureStage.REQUIRE_TEXT) {
			event.setCancelled(true);

			if (event.getMessage().equalsIgnoreCase("cancel")) {
				PunishmentProcedure.getProcedures().remove(procedure);
				event.getPlayer().sendMessage(CC.RED + "You have cancelled the punishment procedure.");
				return;
			}

			if (procedure.getType() == PunishmentProcedureType.PARDON) {
				new ConfirmMenu(CC.YELLOW + "Pardon this punishment?", new TypeCallback<Boolean>() {
					@Override
					public void callback(Boolean data) {
						if (data) {
							procedure.getPunishment().setPardonedBy(event.getPlayer().getUniqueId());
							procedure.getPunishment().setPardonedAt(System.currentTimeMillis());
							procedure.getPunishment().setPardonedReason(event.getMessage());
							procedure.getPunishment().setPardoned(true);
							procedure.finish();

							event.getPlayer().sendMessage(CC.GREEN + "The punishment has been pardoned.");
						} else {
							event.getPlayer().sendMessage(CC.RED + "You did not confirm to pardon the punishment.");
						}
					}
				}, true) {
					@Override
					public void onClose(Player player) {
						if (!isClosedByMenu()) {
							event.getPlayer().sendMessage(CC.RED + "You did not confirm to pardon the punishment.");
						}
					}
				}.openMenu(event.getPlayer());
			}
		}
	}

}
