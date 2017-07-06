package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import ua.Endertainment.QuartzDefenders.Balance;
import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.GUI.GamesGUI;
import ua.Endertainment.QuartzDefenders.GUI.KitsGUI;
import ua.Endertainment.QuartzDefenders.GUI.LobbyShopGUI;
import ua.Endertainment.QuartzDefenders.GUI.StatsGUI;
import ua.Endertainment.QuartzDefenders.Kits.Kit;
import ua.Endertainment.QuartzDefenders.Kits.KitsManager;
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
		
		Player p = (Player) e.getWhoClicked();
		
		/*
		 * Player's inventory
		 */
		if(inv.getType() == InventoryType.PLAYER || inv.getType() == InventoryType.CREATIVE) {
			e.setCancelled(true);
			return;
		}
		
		p.playSound(p.getLocation(), Sound.BLOCK_METAL_PRESSUREPLATE_CLICK_ON, 1F, 2F);		
		/*
		 * GAMES
		 */
		if(inv.getName().equals(new GamesGUI(plugin).getInventory().getName())) {
			e.setCancelled(true);
			
			for(Game game : plugin.getGames()) {
				if(curr.getItemMeta().getDisplayName().equals(game.getGameName())) {			
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
			return;
		}
		/*
		 * SHOP
		 */
		if(inv.getName().equals(new LobbyShopGUI(p).getInventory().getName())) {
			e.setCancelled(true);
			
			for(Kit kit : KitsManager.getInstance().getKits()) {				
				if(curr.getItemMeta().getDisplayName().equals(kit.getItems().get(0).getItemMeta().getDisplayName())) {
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
		if(inv.getName().equals(new KitsGUI(p).getInventory().getName())) {
			e.setCancelled(true);
			
			for(Kit kit : KitsManager.getInstance().getKits()) {
				if(curr.getItemMeta().getDisplayName().equals(kit.getItems().get(0).getItemMeta().getDisplayName())) {
					if(KitsManager.getInstance().isKitAccess(kit, p)) {
						KitsManager.getInstance().chooseKit(kit, plugin.getGame(p), plugin.getGamePlayer(p));
						p.closeInventory();
					} else {KitsManager.getInstance().chooseKitFailed(kit, plugin.getGamePlayer(p)); p.closeInventory();}
				}
			}
			
			return;
		}
		
		/*
		 * TEAMS
		 */
			
		if(inv.getName().equals(new ColorFormat(ChatColor.BLUE + "Teams").format())) {
			e.setCancelled(true);
			
			Game game = plugin.getGame(p);
			
			for(GameTeam team : game.getTeams().values()) {
				
				if(curr.getItemMeta().getDisplayName().equals(team.getColor() + "> " + team.getName() + " <")) {
						
					if(new Balance(game, game.getBalanceType(), plugin.getGamePlayer(p), game.getTeams().values(), team).isTeamsBalanced()) {
						
						team.joinTeam(plugin.getGamePlayer(p));
								
					}
		
					p.closeInventory();
					return;
				}
					
			}								
			return;
		}
		
		
	}
}
