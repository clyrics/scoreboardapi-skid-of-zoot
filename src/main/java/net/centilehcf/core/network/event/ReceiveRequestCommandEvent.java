package net.centilehcf.core.network.event;

import lombok.Getter;
import lombok.Setter;
import net.centilehcf.core.util.BaseEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;

/**
 * Created by DaddyDombo daddydombo@gmail.com on 4/21/2019.
 */
public class ReceiveRequestCommandEvent extends BaseEvent implements Cancellable {

    @Getter
    private Player player;
    @Getter @Setter
    private boolean cancelled;

    public ReceiveRequestCommandEvent(Player player) {
        this.player = player;
    }
}
