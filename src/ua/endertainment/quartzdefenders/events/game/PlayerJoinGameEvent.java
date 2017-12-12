package ua.endertainment.quartzdefenders.Events.Game;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.Game.GameState;
import ua.endertainment.quartzdefenders.game.GamePlayer;

public class PlayerJoinGameEvent extends Event implements Cancellable {
	
	private Game game;
	private GamePlayer gamePlayer;
	private Player player;
	private GameState state;
	
	private boolean cansel;
	
	public PlayerJoinGameEvent(Game game, GamePlayer gamePlayer) {
		this.game = game;
		this.gamePlayer = gamePlayer;
		this.player = gamePlayer.getPlayer();
		this.state = game.getGameState();
		
		this.cansel = false;
	}
	
	public Game getGame() {
		return game;
	}
	public GameState getGameState() {
		return state;
	}
	public GamePlayer getGamePlayer() {
		return gamePlayer;
	}
	public Player getPlayer() {
		return player;
	}

	public boolean isCancelled() {
		return cansel;
	}

	public void setCancelled(boolean paramBoolean) {
		this.cansel = paramBoolean;
	}
	
	public static final HandlerList handlers = new HandlerList();
	
	public HandlerList getHandlers() {
		return handlers;
	}
	public static HandlerList getHandlerList() {
		return handlers;
	}
	
}
