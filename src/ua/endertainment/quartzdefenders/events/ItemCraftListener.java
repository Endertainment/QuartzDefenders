package ua.endertainment.quartzdefenders.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.game.Game;

public class ItemCraftListener implements Listener {

    private QuartzDefenders plugin;

    public ItemCraftListener(QuartzDefenders plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    //Blocking bow craft
    @EventHandler
    public void onCraft(CraftItemEvent event) {
        Game game = plugin.getGame(event.getInventory().getLocation().getWorld());
        if (game != null && game.isRangedBlocked()) {
            Material type = event.getRecipe().getResult().getType();
            if (type.equals(Material.BOW) || type.equals(Material.CROSSBOW)) {
                event.setCancelled(true);
            }
        }
    }

}
