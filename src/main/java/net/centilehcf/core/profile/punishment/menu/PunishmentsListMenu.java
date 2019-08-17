package net.centilehcf.core.profile.punishment.menu;

import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.punishment.procedure.PunishmentProcedure;
import net.centilehcf.core.profile.punishment.procedure.PunishmentProcedureStage;
import net.centilehcf.core.profile.punishment.procedure.PunishmentProcedureType;
import net.centilehcf.core.util.menu.Button;
import net.centilehcf.core.util.menu.pagination.PaginatedMenu;
import net.centilehcf.core.profile.punishment.Punishment;
import net.centilehcf.core.profile.punishment.PunishmentType;
import net.centilehcf.core.util.ItemBuilder;
import net.centilehcf.core.util.TimeUtil;
import net.centilehcf.core.util.CC;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.ItemBuilder;
import net.centilehcf.core.util.TimeUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class PunishmentsListMenu extends PaginatedMenu {

	private Profile profile;
	private PunishmentType punishmentType;

	@Override
	public String getPrePaginatedTitle(Player player) {
		return "&6" + punishmentType.getTypeData().getReadable() + " &7- &f" + profile.getUsername();
	}

	@Override
	public Map<Integer, Button> getAllPagesButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		for (Punishment punishment : profile.getPunishments()) {
			if (punishment.getType() == punishmentType) {
				buttons.put(buttons.size(), new PunishmentInfoButton(punishment));
			}
		}

		return buttons;
	}

	@AllArgsConstructor
	private class PunishmentInfoButton extends Button {

		private Punishment punishment;

		@Override
		public ItemStack getButtonItem(Player player) {
			String addedBy = "Console";

			if (punishment.getAddedBy() != null) {
				try {
					Profile addedByProfile = Profile.getByUuid(punishment.getAddedBy());
					addedBy = addedByProfile.getUsername();
				} catch (Exception e) {
					addedBy = "Could not fetch...";
				}
			}

			List<String> lore = new ArrayList<>();

			lore.add(CC.MENU_BAR);
			lore.add("&eAdded by: &c" + addedBy);
			lore.add("&eAdded for: &c" + punishment.getAddedReason());

			if (punishment.isActive() && !punishment.isPermanent() && punishment.getDuration() != -1) {
				lore.add("&eDuration: &c" + punishment.getTimeRemaining());
			}

			if (punishment.isPardoned()) {
				String removedBy = "Console";

				if (punishment.getPardonedBy() != null) {
					try {
						Profile removedByProfile = Profile.getByUuid(punishment.getPardonedBy());
						removedBy = removedByProfile.getUsername();
					} catch (Exception e) {
						removedBy = "Could not fetch...";
					}
				}

				lore.add(CC.MENU_BAR);
				lore.add("&ePardoned at: &c" + TimeUtil.dateToString(new Date(punishment.getPardonedAt())));
				lore.add("&ePardoned by: &c" + removedBy);
				lore.add("&ePardoned for: &c" + punishment.getPardonedReason());
			}

			lore.add(CC.MENU_BAR);

			if (!punishment.isPardoned() && punishment.getType().isCanBePardoned()) {
				lore.add("&eRight click to pardon this punishment");
				lore.add(CC.MENU_BAR);
			}

			return new ItemBuilder(Material.PAPER)
					.name("&6" + TimeUtil.dateToString(new Date(punishment.getAddedAt())))
					.lore(lore)
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			if (clickType == ClickType.RIGHT && !punishment.isPardoned() && punishment.getType().isCanBePardoned()) {
				PunishmentProcedure procedure = new PunishmentProcedure(player, profile, PunishmentProcedureType.PARDON, PunishmentProcedureStage.REQUIRE_TEXT);
				procedure.setPunishment(punishment);

				player.sendMessage(CC.GREEN + "Type a reason for pardoning this punishment in chat...");
				player.closeInventory();
			}
		}
	}

}
