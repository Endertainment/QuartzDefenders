package ua.endertainment.quartzdefenders.events.game;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ua.endertainment.quartzdefenders.game.Game;

public class GameEndEvent extends Event{

	private Game game;
	
	public GameEndEvent(Game game) {
		this.game = game;
	}
	
	public Game getGame() {
		return game;
	}
	
	public static final HandlerList handlers = new HandlerList();
	
        @Override
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}

}
