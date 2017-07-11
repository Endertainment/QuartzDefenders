package ua.Endertainment.QuartzDefenders.Events;

import java.util.Iterator;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Game.GameState;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.Stats.StatsPlayer;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;
import ua.Endertainment.QuartzDefenders.Utils.LoggerUtil;

public class ChatFormatEvent implements Listener{

	private QuartzDefenders plugin;
	
	public ChatFormatEvent(QuartzDefenders plugin) {
		this.plugin = plugin;
		Bukkit.getPluginManager().registerEvents(this, this.plugin);
	}
	
	@EventHandler
	public void chatForm(AsyncPlayerChatEvent e) {
		StatsPlayer sp = new StatsPlayer(e.getPlayer());
		
		String formatLobby = new ColorFormat("&b{lvl}&8 |&r %1$s &8»&r %2$s").format();
		
		String formatSpectators = new ColorFormat("&8Spect |&r %1$s &8»&r %2$s").format();
		
		String formatGameTeam = new ColorFormat("&8Team |&r %1$s &8»&r %2$s").format();
		
		String formatGameAll = new ColorFormat("&8All |&r %1$s &8»&r %2$s").format();		
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
			e.getPlayer().sendMessage(LoggerUtil.gameMessage("Game", "&cYou can not write in chat here"));
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
				e.getPlayer().sendMessage(LoggerUtil.gameMessage("Game", "&cYou can not write in chat here"));
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
		
//		if(plugin.getGame(e.getPlayer()) != null) {
//			Game game = plugin.getGame(e.getPlayer());
//			if(e.getPlayer().getLocation().getWorld() == game.getGameWorld()) {
//
//				if(game.isPlayerInTeam(plugin.getGamePlayer(e.getPlayer()))) {
//					GameTeam team = game.getTeam(e.getPlayer());
//					if(message.startsWith("!")) {
//						formatGameAll = formatGameAll.replace("%1$s", e.getPlayer().getDisplayName());
//						for(Player pp : e.getRecipients()) {
//							if(!game.getPlayers().contains(plugin.getGamePlayer(pp))) {
//								e.getRecipients().remove(pp);
//							}
//						}
//						e.setFormat(formatGameAll);
//						return;
//					} else {
//						formatGameTeam = formatGameTeam.replace("%1$s", e.getPlayer().getDisplayName());
//						for(Player pp : e.getRecipients()) {
//							if(!team.getPlayers().contains(plugin.getGamePlayer(pp))) {
//								e.getRecipients().remove(pp);
//							}
//						}						
//						e.setFormat(formatGameTeam);
//						return;
//					}
//				} else {
//					if(game.getSpectators().contains(plugin.getGamePlayer(e.getPlayer()))) {
//						formatSpectators = formatSpectators.replace("%1$s", e.getPlayer().getDisplayName());
//						for(Player pp : e.getRecipients()) {
//							if(!game.getSpectators().contains(plugin.getGamePlayer(pp))) {
//								e.getRecipients().remove(pp);
//							}
//						}							
//						e.setFormat(formatSpectators);
//						return;
//					}
//					e.setCancelled(true);
//					e.getPlayer().sendMessage(LoggerUtil.gameMessage("Game", "&cYou can not write in chat here"));
//					return;
//				}
//				
//			} else {
//				formatLobby = formatLobby.replace("{lvl}", sp.getLevel() + "").replace("%1$s", e.getPlayer().getDisplayName());
//				e.setFormat(formatLobby);		
//			}
//		}
	}
	
}
