package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.GUI.KitsGUI;
import ua.Endertainment.QuartzDefenders.GUI.LobbyShopGUI;
import ua.Endertainment.QuartzDefenders.GUI.StatsGUI;
import ua.Endertainment.QuartzDefenders.GUI.TeamGUI;
import ua.Endertainment.QuartzDefenders.Items.QItems;

public class ItemsUseEvent implements Listener{

	private QuartzDefenders plugin;
	
	public ItemsUseEvent(QuartzDefenders plugin) {
		this.plugin = plugin;
		
		Bukkit.getPluginManager().registerEvents(this, plugin);
	}
	
	@EventHandler
    public void fireBlock(ProjectileHitEvent event) {
        if (event.getHitEntity() != null) {
            return;
        }
        if (event.getEntity() instanceof Arrow) {
            Arrow arrow = (Arrow) event.getEntity();
            if (arrow.getFireTicks() > 0) {
                Block block = event.getHitBlock();
                if (block.getRelative(BlockFace.UP).getType() == Material.AIR) {
                    block.getRelative(BlockFace.UP).setType(Material.FIRE);
                } else if (block.getRelative(BlockFace.EAST).getType() == Material.AIR) {
                    block.getRelative(BlockFace.EAST).setType(Material.FIRE);
                } else if (block.getRelative(BlockFace.WEST).getType() == Material.AIR) {
                    block.getRelative(BlockFace.WEST).setType(Material.FIRE);
                } else if (block.getRelative(BlockFace.NORTH).getType() == Material.AIR) {
                    block.getRelative(BlockFace.NORTH).setType(Material.FIRE);
                } else if (block.getRelative(BlockFace.SOUTH).getType() == Material.AIR) {
                    block.getRelative(BlockFace.SOUTH).setType(Material.FIRE);
                } else if (block.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
                    block.getRelative(BlockFace.DOWN).setType(Material.FIRE);
                }
            }
        }
    }
    
	@EventHandler
	public void itemUse(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		
		ItemStack i = e.getItem();
		
		if(i == null || i.getType().equals(Material.AIR)) return;
		
		if(!i.hasItemMeta()) return;
		
		if(e.getAction().equals(Action.RIGHT_CLICK_AIR) || e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			
			String compass = QItems.itemGamesChoose().getItemMeta().getDisplayName();
			String stats = QItems.itemStats().getItemMeta().getDisplayName();
			String kits = QItems.itemKitsChoose().getItemMeta().getDisplayName();
			String quit = QItems.itemQuit().getItemMeta().getDisplayName();
			String show = QItems.itemHidePlayers(true).getItemMeta().getDisplayName();
			String hide = QItems.itemHidePlayers(false).getItemMeta().getDisplayName();
			String lShop = QItems.itemLobbyShop().getItemMeta().getDisplayName();
			
			if(i.getItemMeta().getDisplayName().equalsIgnoreCase(compass)) {
				e.setCancelled(true);
				plugin.getGamesGUI().openInventory(p);
				return;
			}
			
			if(i.getItemMeta().getDisplayName().equalsIgnoreCase(stats)) {
				e.setCancelled(true);
				new StatsGUI(p).openInventory();
				return;
			}	
			
			if(i.getItemMeta().getDisplayName().equalsIgnoreCase(hide)) {
				e.setCancelled(true);
				plugin.getLobby().hidePlayers(p);
				p.getInventory().setItemInMainHand(QItems.itemHidePlayers(true));
				return;
			}
			
			if(i.getItemMeta().getDisplayName().equalsIgnoreCase(show)) {
				e.setCancelled(true);
				plugin.getLobby().hidePlayers(p);
				p.getInventory().setItemInMainHand(QItems.itemHidePlayers(false));
				return;
			}
			
			if(i.getItemMeta().getDisplayName().equalsIgnoreCase(kits)) {
				e.setCancelled(true);				
				new KitsGUI(p).openInventory();				
				return;
			}
			
			if(i.getItemMeta().getDisplayName().equalsIgnoreCase(lShop)) {
				e.setCancelled(true);
				new LobbyShopGUI(p).openInventory();
				return;
			}
			
			if(plugin.getGame(p) != null) {
				String teams = QItems.itemTeamChoose(plugin.getGame(p)).getItemMeta().getDisplayName();
			
				if(i.getItemMeta().getDisplayName().equalsIgnoreCase(teams)) {
					e.setCancelled(true);
					new TeamGUI(plugin.getGame(p)).openInventory(p);				
					return;
				}
			}
			
			if(i.getItemMeta().getDisplayName().equalsIgnoreCase(quit)) {
				e.setCancelled(true);
				plugin.getGame(p).quitGame(plugin.getGamePlayer(p));
				return;
			}
		}
	}
	
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		ItemStack i = e.getItemDrop().getItemStack();
		Player p = e.getPlayer();
		if(!i.hasItemMeta()) return;
		
		if(i.getItemMeta().getDisplayName().equals(QItems.itemGamesChoose().getItemMeta().getDisplayName()) || 
				i.getItemMeta().getDisplayName().equals(QItems.itemStats().getItemMeta().getDisplayName()) || 
				i.getItemMeta().getDisplayName().equals(QItems.itemKitsChoose().getItemMeta().getDisplayName()) ||
				
				i.getItemMeta().getDisplayName().equals(QItems.itemQuit().getItemMeta().getDisplayName()) ||
				i.getItemMeta().getDisplayName().equals(QItems.itemHidePlayers(true).getItemMeta().getDisplayName()) || 
				i.getItemMeta().getDisplayName().equals(QItems.itemHidePlayers(false).getItemMeta().getDisplayName()) || 
				i.getItemMeta().getDisplayName().equals(QItems.itemLobbyShop().getItemMeta().getDisplayName()) 
				
				
				)
		{
			e.setCancelled(true);
			return;
		}
		
		if(plugin.getGame(p) != null) {
			if(i.getItemMeta().getDisplayName().equals(QItems.itemTeamChoose(plugin.getGame(p)))) e.setCancelled(true);;
		}
		
	}
}
