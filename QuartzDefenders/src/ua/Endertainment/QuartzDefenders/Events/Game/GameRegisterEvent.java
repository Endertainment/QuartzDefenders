package ua.Endertainment.QuartzDefenders.Events.Game;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ua.Endertainment.QuartzDefenders.Game;

public class GameRegisterEvent extends Event implements Cancellable {

	private Game game;
	private String gameId;
	
	private boolean isCancelled;
	
	public GameRegisterEvent(Game game) {
		this(game, game.getGameId());
	}
	public GameRegisterEvent(Game game, String gameId) {
		this.game = game;
		this.gameId = gameId;
		
		this.isCancelled = false;
	}
	
	public String getGameId() {
		return gameId;
	}
	public Game getGame() {
		return game;
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
