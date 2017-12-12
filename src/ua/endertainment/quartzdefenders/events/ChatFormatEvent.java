package ua.endertainment.quartzdefenders.events;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.game.Game.GameState;
import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.stats.StatsPlayer;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;

public class ChatFormatEvent implements Listener{

	private QuartzDefenders plugin;
	
	public ChatFormatEvent(QuartzDefenders plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}
	
	@EventHandler
	public void chatForm(AsyncPlayerChatEvent e) {
		StatsPlayer sp = new StatsPlayer(e.getPlayer());
		
		String formatLobby = Language.getString("chat_formats.f_lobby");
		
		String formatSpectators = Language.getString("chat_formats.f_spect");
		
		String formatGameTeam = Language.getString("chat_formats.f_team");
		
		String formatGameAll = Language.getString("chat_formats.f_all");
		
		/*
		 * Color Message
		 */	
		if(e.getPlayer().hasPermission("QuartzDefenders.ColorChat")) e.setMessage(new ColorFormat(e.getMessage()).format());		
		String message = e.getMessage();
		/*
		 * ChatFormat : Lobby
		 */
		if(e.getPlayer().getLocation().getWorld() == plugin.getLobby().getWorld()) {
			formatLobby = formatLobby.replace("{lvl}", sp.getLevel() + "").replace("%1$s", e.getPlayer().getDisplayName());
			
			Iterator<Player> iter = e.getRecipients().iterator();			
			while (iter.hasNext()) {
		        Player p = iter.next();
		        if(!plugin.getLobby().getWorld().getPlayers().contains(p)) {   
		        	iter.remove();
		        }
			}
			
			e.setFormat(formatLobby);
			return;
		}
		/*
		 * ChatFormat : Game
		 */
		if(plugin.getGame(e.getPlayer()) == null) {
			e.setCancelled(true);
			e.getPlayer().sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.chat_failed")));
			return;
		}
		
		Game game = plugin.getGame(e.getPlayer());
		
		if(e.getPlayer().getWorld() != game.getGameWorld()) {
			if(game.isGameState(GameState.LOBBY)) { 
				Iterator<Player> iter = e.getRecipients().iterator();			
				while (iter.hasNext()) {
		        	Player p = iter.next();
		        	if(!plugin.getLobby().getWorld().getPlayers().contains(p)) {   
		        		iter.remove();
		        	}
				}
			} else {
				e.setCancelled(true);
				e.getPlayer().sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.chat_failed")));
			}
			return;
		}
		
		if(e.getPlayer().getWorld() == game.getGameWorld()) {
			
			if(game.isGameState(GameState.WAITING) || game.isGameState(GameState.STARTING) || game.isGameState(GameState.ENDING)) {
				formatLobby = formatLobby.replace("{lvl}", sp.getLevel() + "").replace("%1$s", e.getPlayer().getDisplayName());
				e.setFormat(formatLobby);
				Iterator<Player> iter = e.getRecipients().iterator();			
				while (iter.hasNext()) {
		        	Player p = iter.next();
		        	if(!game.containsPlayer(plugin.getGamePlayer(p))) {   
		        		iter.remove();
		        	}
				}
			}
			
			if(game.isGameState(GameState.ACTIVE)) {
				if(game.getSpectators().contains(plugin.getGamePlayer(e.getPlayer()))) {
					formatSpectators = formatSpectators.replace("%1$s", e.getPlayer().getDisplayName());
					Iterator<Player> iter = e.getRecipients().iterator();			
					while (iter.hasNext()) {
			        	Player p = iter.next();
			        	if(!game.getSpectators().contains(plugin.getGamePlayer(p))) {   
			        		iter.remove();
			        	}
					}					
					e.setFormat(formatSpectators);
					return;
				}
						
				if(message.startsWith("!")) {
					e.setMessage(message.substring(1));
					formatGameAll = formatGameAll.replace("%1$s", e.getPlayer().getDisplayName());
					Iterator<Player> iter = e.getRecipients().iterator();			
					while (iter.hasNext()) {
			        	Player p = iter.next();
			        	if(!game.containsPlayer(plugin.getGamePlayer(p))) {   
			        		iter.remove();
			        	}
					}
					e.setFormat(formatGameAll);
				} else {
					formatGameTeam = formatGameTeam.replace("%1$s", e.getPlayer().getDisplayName());
					Iterator<Player> iter = e.getRecipients().iterator();			
					while (iter.hasNext()) {
			        	Player p = iter.next();
			        	GameTeam team = game.getTeam(e.getPlayer());
			        	if(!team.getPlayers().contains(plugin.getGamePlayer(p))) {   
			        		iter.remove();
			        	}
					}
					e.setFormat(formatGameTeam);
				}
				
			}
			
			return;
		}
	
	}
	
}
