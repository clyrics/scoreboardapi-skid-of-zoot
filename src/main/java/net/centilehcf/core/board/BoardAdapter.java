package net.centilehcf.core.board;

import java.util.List;
import org.bukkit.entity.Player;

public interface BoardAdapter {

	String getTitle(Player player);

	List<String> getLines(Player player);

	void preLoop();

}
