package net.centilehcf.core.profile.grant.event;

import net.centilehcf.core.profile.grant.Grant;
import net.centilehcf.core.util.BaseEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import net.centilehcf.core.util.BaseEvent;
import org.bukkit.entity.Player;

@AllArgsConstructor
@Getter
public class GrantExpireEvent extends BaseEvent {

	private Player player;
	private Grant grant;

}
