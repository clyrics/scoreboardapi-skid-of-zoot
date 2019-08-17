package net.centilehcf.core.board.events;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

@Getter
public class BoardCreateEvent extends Event implements Cancellable {

    @Getter public static HandlerList HANDLER_LIST = new HandlerList();

    private Player player;

    @Setter
    private boolean cancelled = false;

    public BoardCreateEvent(Player player) {
        this.player = player;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
