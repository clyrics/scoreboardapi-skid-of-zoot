package net.centilehcf.core.profile.option.event;

import net.centilehcf.core.profile.option.menu.ProfileOptionButton;
import net.centilehcf.core.util.BaseEvent;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import net.centilehcf.core.profile.option.menu.ProfileOptionButton;
import net.centilehcf.core.util.BaseEvent;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Getter
public class OptionsOpenedEvent extends BaseEvent {

	private final Player player;
	private List<ProfileOptionButton> buttons = new ArrayList<>();

}
