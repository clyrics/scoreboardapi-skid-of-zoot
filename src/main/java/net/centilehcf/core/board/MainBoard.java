package net.centilehcf.core.board;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import lombok.Getter;
import lombok.Setter;

import net.centilehcf.core.Core;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class MainBoard {

	//Instance
	@Getter private static MainBoard instance;

	private JavaPlugin plugin;
	private BoardAdapter adapter;
	private Map<UUID, Board> boards;
	private BoardThread thread;

	//Scoreboard Ticks
	@Setter
	private long ticks = 2;

	//Default Scoreboard Style
	@Setter
	private BoardStyle boardStyle = BoardStyle.MODERN;

	public MainBoard(JavaPlugin plugin, BoardAdapter adapter) {

		if (instance != null) {
			throw new RuntimeException("MainBoard has already been instantiated!");
		}

		if (plugin == null) {
			throw new RuntimeException("MainBoard can not be instantiated without a plugin instance!");
		}

		instance = this;

		this.plugin = plugin;
		this.adapter = adapter;
		this.boards = new ConcurrentHashMap<>();

		this.setup();
	}

	private void setup() {
		//Register Events
		Bukkit.getPluginManager().registerEvents(new BoardListener(), Core.get());

		//Ensure that the thread has stopped running
		if (this.thread != null) {
			this.thread.stop();
			this.thread = null;
		}

		//Start Thread
		this.thread = new BoardThread(this);
	}

}
