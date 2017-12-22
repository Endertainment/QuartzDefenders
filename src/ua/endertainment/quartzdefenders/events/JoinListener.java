package ua.endertainment.quartzdefenders.events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.ScoreboardLobby;

public class JoinListener implements Listener {

	private QuartzDefenders plugin;
	
	public JoinListener(QuartzDefenders quartzDefenders) {
		this.plugin = quartzDefenders;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		plugin.addGamePlayer(p);
		
		p.setDisplayName(ChatColor.GRAY + p.getName() + ChatColor.RESET);
		p.setPlayerListName(ChatColor.GRAY + p.getName() + ChatColor.RESET);
		
		if(p.hasPermission("QuartzDefenders.lobby.colorName")) {
			p.setDisplayName(ChatColor.AQUA + p.getName() + ChatColor.RESET);
			p.setPlayerListName(ChatColor.AQUA + p.getName() + ChatColor.RESET);
		}
		
		for(String s : plugin.getDevs()) {
			if(s.equalsIgnoreCase(p.getName())) {
				p.setDisplayName(ChatColor.DARK_RED + p.getName() + ChatColor.RESET);
				p.setPlayerListName(ChatColor.DARK_RED + p.getName() + ChatColor.RESET);
			}
		}
		
		if(p.hasPermission("QuartzDefenders.lobby.alert.join")) {
			e.setJoinMessage(new ColorFormat("&3» &f[&3+&f] &r" + p.getDisplayName()).format());
		} else {
			e.setJoinMessage("");
		}
		
		p.setGameMode(GameMode.ADVENTURE);
		
		p.setHealth(20);
		p.setFoodLevel(20);
		
		plugin.getLobby().setLobbyTools(p);
		
		for(Player pp : Bukkit.getOnlinePlayers()) {
			new ScoreboardLobby(plugin, pp).setScoreboard();
		}
		
	}
	
}
