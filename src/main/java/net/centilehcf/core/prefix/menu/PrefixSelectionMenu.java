package net.centilehcf.core.prefix.menu;

import net.centilehcf.core.Core;
import net.centilehcf.core.prefix.Prefix;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.ItemBuilder;
import net.centilehcf.core.util.menu.Button;
import net.centilehcf.core.util.menu.Menu;
import lombok.AllArgsConstructor;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PrefixSelectionMenu extends Menu {

    @Override
    public String getTitle(Player player) {
        return ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Choose a prefix.";
    }

    @Override
    public Map<Integer, Button> getButtons(Player player) {
        final Map<Integer, Button> buttons = new HashMap<>();
        Core.get().getPrefixHandler().getPrefixes().sort(Comparator.comparingInt(Prefix::getWeight).reversed());
        Core.get().getPrefixHandler().getPrefixes().forEach(prefix -> {
            buttons.put(buttons.size(), new PrefixSelectionButton(prefix));
        });
        buttons.put(49, new Button() {
            @Override
            public ItemStack getButtonItem(Player player) {
                return new ItemBuilder(Material.REDSTONE_BLOCK).name(CC.RED + "Reset prefix").lore(
                        Arrays.asList(
                                CC.SB_BAR,
                                CC.YELLOW + "Click to remove your prefix",
                                CC.SB_BAR
                        )
                ).build();
            }

            @Override
            public void clicked(Player player, int slot, ClickType clickType, int hotbarButton) {
                Profile profile = Profile.getByUuid(player.getUniqueId());
                if (profile != null) {
                    profile.setPrefix(Core.get().getPrefixHandler().getDefaultPrefix());
                }
                player.sendMessage(CC.YELLOW + "You have reset your prefix.");
            }
        });
        return buttons;
    }

    @AllArgsConstructor
    private static class PrefixSelectionButton extends Button {

        private Prefix prefix;

        @Override
        public ItemStack getButtonItem(Player player) {

            final List<String> lore = new ArrayList<>();

            lore.add(0, CC.SB_BAR);
            lore.add(CC.YELLOW + "Shows as " + prefix.getPrefix());
            lore.add(CC.SB_BAR);

            byte color;

            if (Profile.getByUuid(player.getUniqueId()).getPrefix().equals(prefix)) {
                color = DyeColor.GREEN.getData();
            } else if (player.hasPermission("prefix." + prefix.getName())) {
                color = DyeColor.SILVER.getData();
            } else {
                color = DyeColor.RED.getData();
            }

            return new ItemBuilder(Material.WOOL)
                    .name(ChatColor.GOLD + prefix.getName())
                    .lore(lore)
                    .durability(color)
                    .build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            if (!player.hasPermission("prefix." + prefix.getName())) {
                player.sendMessage(CC.RED + "You don't have access for that prefix! Purchase it on the store!");
                return;
            }

            player.closeInventory();

            player.sendMessage(CC.GREEN + "You have updated your prefix to: " + CC.WHITE + prefix.getName());

            Profile.getByUuid(player.getUniqueId()).setPrefix(prefix);
        }
    }
}
