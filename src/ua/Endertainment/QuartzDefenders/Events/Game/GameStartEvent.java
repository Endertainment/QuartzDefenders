package ua.Endertainment.QuartzDefenders.Events.Game;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ua.Endertainment.QuartzDefenders.Game;

public class GameStartEvent extends Event implements Cancellable{

	private Game game;
	
	private boolean isCancelled;
	
	public GameStartEvent(Game game) {
		this.game = game;
		
		this.isCancelled = false;
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
	
        @Override
	public boolean isCancelled() {
		return isCancelled;
	}
	
        @Override
	public void setCancelled(boolean paramBoolean) {
		this.isCancelled = paramBoolean;		
	}
	

}
