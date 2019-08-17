package net.centilehcf.core.profile.grant.menu;

import net.centilehcf.core.Core;
import net.centilehcf.core.network.packet.PacketAddGrant;
import net.centilehcf.core.profile.Profile;
import net.centilehcf.core.profile.grant.Grant;
import net.centilehcf.core.profile.grant.event.GrantAppliedEvent;
import net.centilehcf.core.rank.Rank;
import net.centilehcf.core.rank.comparator.RankComparator;
import net.centilehcf.core.util.BukkitUtils;
import net.centilehcf.core.util.CC;
import net.centilehcf.core.util.ItemBuilder;
import net.centilehcf.core.util.menu.Button;
import net.centilehcf.core.util.menu.menus.ConfirmMenu;
import net.centilehcf.core.util.menu.pagination.PaginatedMenu;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 7/20/2019.
 */
public class RankSelectionMenu extends PaginatedMenu
{
    private Profile profile;

    @Override
    public String getPrePaginatedTitle(Player player) {
        return CC.YELLOW + CC.BOLD + "Chose a rank.";
    }

    @Override
    public Map<Integer, Button> getAllPagesButtons(Player player) {
        Map<Integer, Button> buttons = new HashMap<>();

        Rank.getRanks().values().stream().sorted(new RankComparator()).collect(Collectors.toList()).forEach(rank -> buttons.put(buttons.size(), new RankDisplayButton(rank, this.profile)));
        return buttons;
    }

    public RankSelectionMenu(Profile profile) {
        this.profile = profile;
    }

    private static class RankDisplayButton extends Button
    {
        private Rank rank;
        private Profile targetData;

        @Override
        public ItemStack getButtonItem(Player player) {
            List<String> lore = new ArrayList<>();
            lore.add(CC.SB_BAR);
            lore.add(ChatColor.BLUE + "Click to grant " + this.targetData.getPlayer().getName() + ChatColor.BLUE + " the " + this.rank.getDisplayName() + ChatColor.BLUE + " rank.");
            lore.add(CC.SB_BAR);
            ChatColor chatColor = ChatColor.getByChar(this.rank.getColor().getChar());
            DyeColor color = BukkitUtils.toDyeColor(chatColor);
            short dura = (color == null) ? 0 : ((short)color.getWoolData());
            return new ItemBuilder(Material.WOOL).name(ChatColor.GOLD + this.rank.getDisplayName()).durability(dura).lore(lore).build();
        }

        @Override
        public void clicked(Player player, int slot, ClickType clickType, int hotbarSlot) {
            player.closeInventory();
            Button button = new Button() {
                @Override
                public ItemStack getButtonItem(Player player) {
                    return new ItemBuilder(Material.PAPER).name(CC.YELLOW + "Are you sure you want to grant " + RankDisplayButton.this.targetData.getPlayer().getName() + " " + RankDisplayButton.this.rank.getDisplayName() + "?").lore(Arrays.asList(CC.SB_BAR, CC.YELLOW + "Rank: " + RankDisplayButton.this.rank.getDisplayName(), CC.SB_BAR, CC.YELLOW + "Are you sure you want", CC.YELLOW + "grant " + RankDisplayButton.this.rank.getDisplayName(), CC.YELLOW + "to " + RankDisplayButton.this.targetData.getPlayer().getName() + "?", CC.SB_BAR)).build();
                }
            };
            Button[] middleButtons = { button, button, button };
            final Grant[] grant = new Grant[1];
            new ConfirmMenu("Confirm grant?", data -> {
                if (data) {
                    player.sendMessage(CC.GREEN + "You have updated " + this.targetData.getPlayer().getName() + CC.GREEN + " rank to: " + this.rank.getDisplayName());
                    grant[0] = new Grant(UUID.randomUUID(), this.rank, player.getUniqueId(), System.currentTimeMillis(), "Granted", Integer.MAX_VALUE);
                    this.targetData.getGrants().add(grant[0]);
                    this.targetData.save();
                    this.targetData.activateNextGrant();
                    Core.get().getPidgin().sendPacket(new PacketAddGrant(this.targetData.getUuid(), grant[0]));
                    new GrantAppliedEvent(player, grant[0]).call();
                }
                else {
                    player.sendMessage(CC.RED + "Cancelled the grant procedure for " + this.targetData.getPlayer().getName() + '.');
                }
            }, true, middleButtons).openMenu(player);
        }

        public RankDisplayButton(Rank rank, Profile targetData) {
            this.rank = rank;
            this.targetData = targetData;
        }
    }
}

