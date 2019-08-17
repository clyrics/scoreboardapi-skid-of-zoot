package net.centilehcf.core.essentials.listener;

import net.centilehcf.core.Core;
import net.centilehcf.core.bootstrap.BootstrappedListener;
import net.centilehcf.core.util.CC;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockFadeEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

public class EssentialsListener extends BootstrappedListener {

    public EssentialsListener(Core core) {
        super(core);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandProcess(PlayerCommandPreprocessEvent event) {
        Player player = event.getPlayer();
        final String lowercase = event.getMessage().toLowerCase();
        if (lowercase.startsWith("//calc") ||
                lowercase.startsWith("//eval") ||
                lowercase.startsWith("//solve") ||
                lowercase.startsWith("/me") ||
                lowercase.startsWith("/bukkit:me") ||
                lowercase.startsWith("/minecraft:") ||
                lowercase.startsWith("/minecraft:me")) {
            player.sendMessage(CC.WHITE + "Unknown command.");
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void foodFix(FoodLevelChangeEvent event) {
        if ((event.getEntity() instanceof Player)) {
            Player player = (Player) event.getEntity();
            player.setSaturation(10.0F);
        }
    }

    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void onBlockFade(BlockFadeEvent event) {
        if (event.getBlock().getType() == Material.ICE || event.getBlock().getType() == Material.PACKED_ICE || event.getBlock().getType() == Material.SNOW_BLOCK) {
            event.setCancelled(true);
        }
    }
}
