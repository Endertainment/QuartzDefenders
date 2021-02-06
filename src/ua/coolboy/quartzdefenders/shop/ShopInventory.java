package ua.coolboy.quartzdefenders.shop;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.Merchant;
import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.MoreTags;

public class ShopInventory implements Listener {

    QuartzDefenders plugin;
    Shop shop;

    public ShopInventory(QuartzDefenders plugin) {
        this.plugin = plugin;
        this.shop = new Shop(plugin.getConfigs().getShopInfo());
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @SuppressWarnings("incomplete-switch")
	@EventHandler
    public void onClick(InventoryClickEvent e) {
        if (!(e.getView().getTitle().contains(Shop.name))) {
            return;
        }
        Player p = (Player) e.getWhoClicked();
        e.setCancelled(true);
        GameTeam team = plugin.getGame(p).getTeam(p);

        if (e.getCurrentItem() == null
                || MoreTags.STAINED_GLASS.isTagged(e.getCurrentItem().getType())
                || Material.AIR == e.getCurrentItem().getType()
                || !e.getCurrentItem().hasItemMeta()) {
            p.closeInventory();
            return;
        }
        switch (e.getCurrentItem().getType()) {
            case EXPERIENCE_BOTTLE:
                Merchant enchants = shop.getSection("enchants", ChatColor.LIGHT_PURPLE + Shop.enchantName, team);
                p.openMerchant(enchants, true);
                break;
            case POTION:
                Merchant potions = shop.getSection("potions", ChatColor.AQUA + Shop.potionsName, team);
                p.openMerchant(potions, true);
                break;
            case BREAD:
                Merchant food = shop.getSection("food", ChatColor.BLUE + Shop.foodName, team);
                p.openMerchant(food, true);
                break;
            case NAME_TAG:
                Merchant other = shop.getSection("other", ChatColor.RED + Shop.otherName, team);
                p.openMerchant(other, true);
                break;
            case BRICK:
                Merchant blocks = shop.getSection("blocks", ChatColor.GRAY + Shop.blocksName, team);
                p.openMerchant(blocks, true);
                break;
            case ARROW:
                Merchant stuff = shop.getSection("stuff", ChatColor.YELLOW + Shop.stuffName, team);
                p.openMerchant(stuff, true);
                break;
            case DIAMOND_PICKAXE:
                Merchant resources = shop.getSection("resources", ChatColor.GREEN + Shop.resourcesName, team);
                p.openMerchant(resources, true);
                break;
        }
    }

    @EventHandler
    public void shopClick(PlayerInteractEntityEvent e) {
        if (e.getRightClicked() instanceof Villager
                && e.getRightClicked().getName().contains(Shop.name)
                && !e.getPlayer().getGameMode().equals(GameMode.SPECTATOR)) {
            e.setCancelled(true);
            Player p = e.getPlayer();
            p.openInventory(shop.getInventory(plugin.getGame(p).getTeam(p)));
        }
    }
}
