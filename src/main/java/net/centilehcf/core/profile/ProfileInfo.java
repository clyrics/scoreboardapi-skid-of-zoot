package net.centilehcf.core.profile;

import net.centilehcf.core.Core;
import net.centilehcf.core.util.BukkitReflection;
import org.bukkit.entity.Player;
import java.util.UUID;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 3/29/2019.
 */
public class ProfileInfo {
    private UUID uuid;
    private String name;

    public ProfileInfo(Player player) {
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public ProfileInfo(UUID uuid, String name) {
        this.uuid = uuid;
        this.name = name;
    }

    public Player toPlayer() {
        Player player = Core.get().getServer().getPlayer(this.getUuid());
        return player != null && player.isOnline() ? player : null;
    }

    public String getDisplayName() {
        Player player = this.toPlayer();
        return player == null ? this.getName() : player.getDisplayName();
    }

    public int getPing() {
        Player player = Core.get().getServer().getPlayer(this.getUuid());
        return player == null ? 0 : BukkitReflection.getPing(player);
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
