package net.centilehcf.core.profile.option.menu;

import net.centilehcf.core.profile.option.event.OptionsOpenedEvent;
import net.centilehcf.core.profile.option.menu.button.PrivateChatOptionButton;
import net.centilehcf.core.profile.option.menu.button.PrivateChatSoundsOptionButton;
import net.centilehcf.core.profile.option.menu.button.PublicChatOptionButton;
import net.centilehcf.core.util.menu.Button;
import net.centilehcf.core.util.menu.Menu;
import java.util.HashMap;
import java.util.Map;

import net.centilehcf.core.util.menu.Menu;
import org.bukkit.entity.Player;

public class ProfileOptionsMenu extends Menu {

	@Override
	public String getTitle(Player player) {
		return "&6&lOptions";
	}

	@Override
	public Map<Integer, Button> getButtons(Player player) {
		Map<Integer, Button> buttons = new HashMap<>();
		buttons.put(buttons.size(), new PublicChatOptionButton());
		buttons.put(buttons.size(), new PrivateChatOptionButton());
		buttons.put(buttons.size(), new PrivateChatSoundsOptionButton());

		OptionsOpenedEvent event = new OptionsOpenedEvent(player);
		event.call();

		if (!event.getButtons().isEmpty()) {
			for (ProfileOptionButton button : event.getButtons()) {
				buttons.put(buttons.size(), button);
			}
		}

		return buttons;
	}

}
