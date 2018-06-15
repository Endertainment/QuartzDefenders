package ua.endertainment.quartzdefenders.events;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.CraftItemEvent;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.game.Game;

public class ItemCraftListener implements Listener {

    private QuartzDefenders plugin;

    public ItemCraftListener(QuartzDefenders plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    //Blocking bow craft
    @EventHandler
    public void onCraft(CraftItemEvent event) {
        Game game = plugin.getGame((Player) event.getWhoClicked());
        if (game !=null && game.isBowBlocked()) {
            if (event.getRecipe().getResult().getType().equals(Material.BOW)) {
                event.setCancelled(true);
            }
        }
    }

}
