package ua.Coolboy.QuartzDefenders.Shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Merchant;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class ShopInventory implements Listener {
    QuartzDefenders plugin;
    Shop shop;
    
    public ShopInventory(QuartzDefenders plugin) {
        this.plugin = plugin;
        this.shop = new Shop(plugin);
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getInventory().getName().contains(shop.name))) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        e.setCancelled(true);
        //GameTeam team = plugin.getGame(p).getTeam(p);

        if (e.getCurrentItem() == null
                || e.getCurrentItem().getType() == Material.STAINED_GLASS
                || e.getCurrentItem().getType() == Material.AIR
                || !e.getCurrentItem().hasItemMeta()) {
            p.closeInventory();
            return;
        }
        switch (e.getCurrentItem().getType()) {
            case EXP_BOTTLE:
    		Merchant enchants = shop.getSection("enchants", ChatColor.LIGHT_PURPLE + shop.enchantName/*, team*/);
                p.openMerchant(enchants, true);
                break;
            case POTION:
    		Merchant potions = shop.getSection("potions", ChatColor.AQUA + shop.potionsName/*, team*/);
                p.openMerchant(potions, true);
                break;
            case BREAD:
    		Merchant food = shop.getSection("food", ChatColor.BLUE + shop.foodName/*, team*/);
                p.openMerchant(food, true);
                break;
            case NAME_TAG:
    		Merchant other = shop.getSection("other", ChatColor.BLUE + shop.otherName/*, team*/);
                p.openMerchant(other, true);
                break;
            case BRICK:
    		Merchant blocks = shop.getSection("blocks", ChatColor.GRAY + shop.blocksName/*, team*/);
                p.openMerchant(blocks, true);
                break;
            case ARROW:
    		Merchant stuff = shop.getSection("stuff", ChatColor.YELLOW + shop.stuffName/*, team*/);
                p.openMerchant(stuff, true);
                break;
            case DIAMOND_PICKAXE:
                Merchant resources = shop.getSection("resources", ChatColor.GREEN + shop.resourcesName/*, team*/);
                p.openMerchant(resources, true);
                break;
        }
    }
    
    @EventHandler
    public void shopClick(PlayerInteractEntityEvent e) {
        if(e.getRightClicked() instanceof Villager 
                /*&& e.getRightClicked().getName().contains(shop.name)*/) {
            e.setCancelled(true);
            Player p = e.getPlayer();
            p.openInventory(shop.getInventory());
        }
    }
}
