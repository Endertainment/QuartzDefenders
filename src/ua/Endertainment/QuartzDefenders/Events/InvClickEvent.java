package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Utils.ColorFormat;

public class InvClickEvent implements Listener {

	private QuartzDefenders plugin;
	
	
	public InvClickEvent(QuartzDefenders plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	
	@EventHandler
	public void onInvClick(InventoryClickEvent e) {
		
		Inventory inv = e.getInventory();
		
		if(e.getCurrentItem() == null || e.getCurrentItem().getType().equals(Material.AIR)) return;
		
		if(!e.getCurrentItem().hasItemMeta()) return;
		
		ItemStack curr = e.getCurrentItem();
		
		/*
		 * GAMES
		 */
		if(inv.getName().equals(plugin.getGamesGUI().getName())) {
			e.setCancelled(true);
			
			for(Game game : plugin.getGames()) {
				if(curr.getItemMeta().getDisplayName().equals(game.getGameName())) {
					
					Player p = (Player) e.getWhoClicked();
					
					game.joinGame(plugin.getGamePlayer(p));
					
					break;
				}
			}		
			
			return;
		}
		
		/*
		 * STATS
		 */
		if(inv.getName().equals(new ColorFormat("&6My Stats").format())) {
			e.setCancelled(true);
			return;
		}
		
		/*
		 * TEAMS
		 */
		for(Game game : plugin.getGames()) {
			if(inv.getName().equals(new ColorFormat(game.getGameName() + " " + ChatColor.BLUE + "Teams").format())) {
				
				return;
			}
		}
		
	}
}
