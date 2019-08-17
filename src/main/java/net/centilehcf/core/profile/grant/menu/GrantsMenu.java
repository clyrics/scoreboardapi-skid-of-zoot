package net.centilehcf.core.profile.grant.menu;

import net.centilehcf.core.profile.grant.Grant;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.grant.procedure.GrantProcedure;
import net.centilehcf.core.profile.grant.procedure.GrantProcedureStage;
import net.centilehcf.core.profile.grant.procedure.GrantProcedureType;
import net.centilehcf.core.util.ItemBuilder;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.menu.Button;
import net.centilehcf.core.util.menu.pagination.PaginatedMenu;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import net.centilehcf.core.profile.grant.procedure.GrantProcedure;
import net.centilehcf.core.profile.grant.procedure.GrantProcedureStage;
import net.centilehcf.core.profile.grant.procedure.GrantProcedureType;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class GrantsMenu extends PaginatedMenu {

	private Profile profile;

	@Override
	public String getPrePaginatedTitle(Player player) {
		return "&6Grants of " + profile.getUsername();
	}

	@Override
	public Map<Integer, Button> getAllPagesButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		for (Grant grant : profile.getGrants()) {
			buttons.put(buttons.size(), new GrantInfoButton(profile, grant));
		}

		return buttons;
	}

	@AllArgsConstructor
	private class GrantInfoButton extends Button {

		private Profile profile;
		private Grant grant;

		@Override
		public ItemStack getButtonItem(Player player) {
			String addedBy = "Console";

			if (grant.getAddedBy() != null) {
				addedBy = "Could not fetch...";

				Profile addedByProfile = Profile.getByUuid(grant.getAddedBy());

				if (addedByProfile != null && addedByProfile.isLoaded()) {
					addedBy = addedByProfile.getUsername();
				}
			}

			List<String> lore = new ArrayList<>();

			lore.add(CC.MENU_BAR);
			lore.add("&eAdded by: &c" + addedBy);
			lore.add("&eAdded for: &c" + grant.getAddedReason());
			lore.add("&eAdded at: &c" + grant.getAddedAtDate());

			if (!grant.isRemoved()) {
				if (!grant.hasExpired()) {
					lore.add("&eExpires at: &c" + grant.getExpiresAtDate());
				}
			} else {
				String removedBy = "Console";

				if (grant.getRemovedBy() != null) {
					removedBy = "Could not fetch...";

					Profile removedByProfile = Profile.getByUuid(grant.getRemovedBy());

					if (removedByProfile != null && removedByProfile.isLoaded()) {
						removedBy = removedByProfile.getUsername();
					}
				}

				lore.add(CC.MENU_BAR);
				lore.add("&eRemoved by: &c" + removedBy);
				lore.add("&eRemoved for: &c" + grant.getRemovedReason());
				lore.add("&eRemoved at: &c" + grant.getRemovedAtDate());
			}

			lore.add(CC.MENU_BAR);

			if (!grant.isRemoved()) {
				lore.add("&eRight click to remove this grant");
				lore.add(CC.MENU_BAR);
			}

			return new ItemBuilder(Material.PAPER)
					.name(grant.getRank().getColor() + grant.getRank().getDisplayName())
					.lore(lore)
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			if (clickType == ClickType.RIGHT && !grant.isRemoved()) {
				GrantProcedure procedure = new GrantProcedure(player, profile, GrantProcedureType.REMOVE, GrantProcedureStage.REQUIRE_TEXT);
				procedure.setGrant(grant);

				player.sendMessage(CC.GREEN + "Type a reason for removing this grant in chat...");
				player.closeInventory();
			}
		}
	}

}
