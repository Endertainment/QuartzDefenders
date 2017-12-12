package ua.endertainment.quartzdefenders.Events.Game;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.Game.GameState;

public class GameStateChangeEvent extends Event implements Cancellable{

	private Game game;
	private GameState stateFrom;
	private GameState stateTo;
	
	private boolean isCancelled;
	
	public GameStateChangeEvent(Game game, GameState from, GameState to) {
		this.game = game;
		this.stateFrom = from;
		this.stateTo = to;
		
		this.isCancelled = false;
	}
	
	public Game getGame() {
		return game;
	}
	
	public GameState getGameStateFrom() {
		return stateFrom;
	}
	public GameState getGameStateTo() {
		return stateTo;
	}
	
	public static final HandlerList handlers = new HandlerList();
	
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
	public boolean isCancelled() {
		return isCancelled;
	}
	
	public void setCancelled(boolean paramBoolean) {
		this.isCancelled = paramBoolean;		
	}
	

}
