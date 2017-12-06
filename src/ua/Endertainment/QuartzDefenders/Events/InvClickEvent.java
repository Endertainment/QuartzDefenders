package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ua.Endertainment.QuartzDefenders.Balance;
import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.GUI.StatsGUI;
import ua.Endertainment.QuartzDefenders.Items.QItems;
import ua.Endertainment.QuartzDefenders.Kits.Kit;
import ua.Endertainment.QuartzDefenders.Kits.KitsManager;
import ua.Endertainment.QuartzDefenders.Utils.Language;
import ua.Endertainment.QuartzDefenders.Utils.Replacer;

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
		
		Player p = (Player) e.getWhoClicked();
		
		
		/*
		 * GAMES
		 */
		if(inv.getName().equals(Language.getString("GUI.games.name"))) {
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, 1F, 2F);		
			
			for(Game game : plugin.getGames()) {
				if(curr.getItemMeta().getDisplayName().equals(game.getColorWorldName())) {			
					if(!game.containsPlayer(plugin.getGamePlayer(p))) {
						game.joinGame(plugin.getGamePlayer(p));
					} else {
						game.quitGame(plugin.getGamePlayer(p));
					}
					p.closeInventory();
					break;
				}
			}					
			return;
		}
		
		/*
		 * STATS
		 */
		if(inv.getName().equals(new StatsGUI(p).getInventory().getName())) {
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, 1F, 2F);		
			
			return;
		}
		/*
		 * SHOP
		 */
		if(inv.getName().equals(Language.getString("GUI.shop.name"))) {
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, 1F, 2F);		
			for(Kit kit : KitsManager.getInstance().getKits()) {		
				if(curr.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', kit.getDisplayName()))) {
					if(KitsManager.getInstance().isKitAccessToBuy(kit, p)) {
						KitsManager.getInstance().buyKit(kit, p);
						p.closeInventory();
					} else {KitsManager.getInstance().buyKitFailed(kit, p); p.closeInventory();}
				}
			}
			
			return;
		}
		
		/*
		 * KITS
		 */
		if(inv.getName().equals(Language.getString("GUI.kits.name"))) {
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, 1F, 2F);		
			for(Kit kit : KitsManager.getInstance().getKits()) {
				if(curr.getItemMeta().getDisplayName().equals(ChatColor.translateAlternateColorCodes('&', kit.getDisplayName()))) {
					if(KitsManager.getInstance().isKitAccess(kit, p)) {						
						KitsManager.getInstance().chooseKit(kit, plugin.getGame(p), plugin.getGamePlayer(p));
						p.closeInventory();
					} else {KitsManager.getInstance().chooseKitFailed(kit, plugin.getGamePlayer(p)); p.closeInventory();}
				}
			}
			
			return;
		}
		
		/*
		 * ACHIEVEMENTS
		 */
		if(inv.getName().equals(Language.getString("GUI.achievements.name"))) {
			e.setCancelled(true);
			p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, 1F, 2F);		
			return;
		}
		
		/*
		 * TEAMS
		 */
			
		if(inv.getName().equals(Language.getString("GUI.teams.name"))) {
			e.setCancelled(true);
			
			p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, 1F, 2F);		
			
			Game game = plugin.getGame(p);

			if(curr.getItemMeta().getDisplayName().equalsIgnoreCase(Language.getString("GUI.teams.item_leave"))) {
				
				if(game.getTeam(p) != null) {
					game.getTeam(p).quitTeam(plugin.getGamePlayer(p));
				}
				return;
				
			}
			
			for(GameTeam team : game.getTeams().values()) {
				
				if(curr.getItemMeta().getDisplayName().equals(Language.getString("GUI.teams.item_name", new Replacer("{c}", team.getColor() + ""), new Replacer("{0}", team.getName())))) {
						
					if(new Balance(game, game.getBalanceType(), plugin.getGamePlayer(p), game.getTeams().values(), team).isTeamsBalanced()) {
						
						team.joinTeam(plugin.getGamePlayer(p), false);
								
					}
		
					p.closeInventory();
					return;
				}
					
			}								
			return;
		}
		
		/*
		 * Player's inventory
		 */
		if(!curr.getItemMeta().hasDisplayName()) {
			return;
		}
		for(ItemStack item : QItems.getAllTechItems()) {
			if(curr.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName())) {
				e.setCancelled(true);
				return;
			}
					
		}

		
	}
}
