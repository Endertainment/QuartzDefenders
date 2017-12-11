package ua.Endertainment.QuartzDefenders.Events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.util.Vector;

import ua.Endertainment.QuartzDefenders.Game.Game;
import ua.Endertainment.QuartzDefenders.Game.Game.GameState;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class DamageEvent implements Listener {

    private QuartzDefenders plugin;

    public DamageEvent(QuartzDefenders plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) {
            if (e.getEntity() instanceof Villager) {
                e.setCancelled(true);
            }
            return;
        }

        Player p = (Player) e.getEntity();

        Game game = plugin.getGame(p);

        if (game == null) {
            return;
        }
        if (p.getWorld() != game.getGameWorld()) {
            return;
        }
        if (game.isGameState(GameState.WAITING) || game.isGameState(GameState.STARTING) || game.isGameState(GameState.ENDING)) {
            e.setCancelled(true);
            if (e.getCause().equals(DamageCause.VOID)) {
                e.getEntity().setVelocity(new Vector(0, 0, 0));
                p.getPlayer().teleport(game.getMapCenter());
            }
        }

    }

    @EventHandler
    public void onFirework(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Firework && event.getEntity() instanceof Player) {
            event.setCancelled(true);
        }
    }
    
}
