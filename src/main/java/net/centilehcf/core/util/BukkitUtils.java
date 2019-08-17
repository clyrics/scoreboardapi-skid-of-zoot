package net.centilehcf.core.util;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffectType;

import java.util.Map;
import java.util.UUID;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 7/20/2019.
 */
public class BukkitUtils {

    public static final UUID CONSOLE_UUID = UUID.fromString("f78a4d8d-d51b-4b39-98a3-230f2de0c670");

    private static final ImmutableMap<ChatColor, DyeColor> CHAT_DYE_COLOUR_MAP = Maps.immutableEnumMap((Map) ImmutableMap.builder()
            .put(ChatColor.AQUA, DyeColor.LIGHT_BLUE)
            .put(ChatColor.BLACK, DyeColor.BLACK)
            .put(ChatColor.BLUE, DyeColor.LIGHT_BLUE)
            .put(ChatColor.DARK_AQUA,DyeColor.CYAN)
            .put(ChatColor.DARK_BLUE,DyeColor.BLUE)
            .put(ChatColor.DARK_GRAY,DyeColor.GRAY)
            .put(ChatColor.DARK_GREEN,DyeColor.GREEN)
            .put(ChatColor.DARK_PURPLE,DyeColor.PURPLE)
            .put(ChatColor.DARK_RED,DyeColor.RED)
            .put(ChatColor.GOLD,DyeColor.ORANGE)
            .put(ChatColor.GRAY,DyeColor.SILVER)
            .put(ChatColor.GREEN,DyeColor.LIME)
            .put(ChatColor.LIGHT_PURPLE, DyeColor.MAGENTA)
            .put(ChatColor.RED, DyeColor.RED)
            .put(ChatColor.WHITE, DyeColor.WHITE)
            .put(ChatColor.YELLOW, DyeColor.YELLOW)
            .build()
    );

    public static String getName(PotionEffectType potionEffectType) {
        if (potionEffectType.getName().equalsIgnoreCase("fire_resistance")) {
            return "Fire Resistance";
        } else if (potionEffectType.getName().equalsIgnoreCase("speed")) {
            return "Speed";
        } else if (potionEffectType.getName().equalsIgnoreCase("weakness")) {
            return "Weakness";
        } else if (potionEffectType.getName().equalsIgnoreCase("slowness")) {
            return "Slowness";
        } else {
            return "Unknown";
        }
    }

    public static Player getDamager(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            return (Player) event.getDamager();
        } else if (event.getDamager() instanceof Projectile) {
            if (((Projectile) event.getDamager()).getShooter() instanceof Player) {
                return (Player) ((Projectile) event.getDamager()).getShooter();
            }
        }

        return null;
    }

    public static DyeColor toDyeColor(ChatColor color) {
        return CHAT_DYE_COLOUR_MAP.get(color);
    }

}

