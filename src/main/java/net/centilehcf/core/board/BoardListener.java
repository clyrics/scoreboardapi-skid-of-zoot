package net.centilehcf.core.board;

import net.centilehcf.core.board.events.BoardCreateEvent;
import net.centilehcf.core.board.events.BoardDestroyEvent;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

public class BoardListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        final BoardCreateEvent createEvent = new BoardCreateEvent(event.getPlayer());

        Bukkit.getPluginManager().callEvent(createEvent);

        if (createEvent.isCancelled()) {
            return;
        }

        MainBoard.getInstance().getBoards().put(event.getPlayer().getUniqueId(), new Board(event.getPlayer()));
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {

        final BoardDestroyEvent destroyEvent = new BoardDestroyEvent(event.getPlayer());

        Bukkit.getPluginManager().callEvent(destroyEvent);

        if (destroyEvent.isCancelled()) {
            return;
        }

        MainBoard.getInstance().getBoards().remove(event.getPlayer().getUniqueId());
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {

        if (MainBoard.getInstance().getThread() == null) {
            return;
        }

        MainBoard.getInstance().getThread().stop();
    }

}