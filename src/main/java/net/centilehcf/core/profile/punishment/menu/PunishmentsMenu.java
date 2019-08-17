package net.centilehcf.core.profile.punishment.menu;

import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.punishment.PunishmentType;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.ItemBuilder;
import net.centilehcf.core.util.menu.Button;
import net.centilehcf.core.util.menu.Menu;
import java.util.HashMap;
import java.util.Map;
import lombok.AllArgsConstructor;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.ItemBuilder;
import net.centilehcf.core.util.menu.Menu;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

@AllArgsConstructor
public class PunishmentsMenu extends Menu {

	private Profile profile;

	@Override
	public String getTitle(Player player) {
		return "&6Punishments of " + profile.getUsername();
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();

		buttons.put(9, new SelectPunishmentTypeButton(profile, PunishmentType.BLACKLIST));
		buttons.put(11, new SelectPunishmentTypeButton(profile, PunishmentType.BAN));
		buttons.put(13, new SelectPunishmentTypeButton(profile, PunishmentType.MUTE));
		buttons.put(15, new SelectPunishmentTypeButton(profile, PunishmentType.WARN));
		buttons.put(17, new SelectPunishmentTypeButton(profile, PunishmentType.KICK));

		return buttons;
	}

	@Override
	public int getSize() {
		return 27;
	}

	@AllArgsConstructor
	private class SelectPunishmentTypeButton extends Button {

		private Profile profile;
		private PunishmentType punishmentType;

		@Override
		public ItemStack getButtonItem(Player player) {
			return new ItemBuilder(Material.WOOL)
					.name(punishmentType.getTypeData().getColor() + CC.BOLD + punishmentType.getTypeData().getReadable())
					.lore(CC.GRAY + profile.getPunishmentCountByType(punishmentType) + " " + (punishmentType.getTypeData().getReadable().toLowerCase()) + " on record")
					.durability(punishmentType.getTypeData().getDurability())
					.build();
		}

		@Override
		public void clicked(Player player, ClickType clickType) {
			new PunishmentsListMenu(profile, punishmentType).openMenu(player);
		}

	}

}
