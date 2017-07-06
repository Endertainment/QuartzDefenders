package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Items.QItems;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;
import ua.Endertainment.QuartzDefenders.Utils.ScoreboardLobby;

public class JoinEvent implements Listener {

	private QuartzDefenders plugin;
	
	public JoinEvent(QuartzDefenders quartzDefenders) {
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
		
		if(p.hasPermission("QuartzDefenders.lobby.joinAlert")) {
			e.setJoinMessage(new ColorFormat("&3» &f[&3+&f] &r" + p.getDisplayName()).format());
		} else {
			e.setJoinMessage("");
		}
		
		p.setGameMode(GameMode.ADVENTURE);
		
		p.setHealth(20);
		p.setFoodLevel(20);
		
		p.getInventory().clear();
		
		p.getInventory().setItem(0, QItems.itemGamesChoose());
		p.getInventory().setItem(4, QItems.itemStats());
		p.getInventory().setItem(7, QItems.itemHidePlayers(plugin.getLobby().getHides().contains(p)));
		p.getInventory().setItem(8, QItems.itemLobbyShop());
		
		ScoreboardLobby s = new ScoreboardLobby(plugin, p);
		s.setScoreboard();
	}
	
}
