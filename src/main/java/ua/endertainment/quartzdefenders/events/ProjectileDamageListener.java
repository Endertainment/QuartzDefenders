package ua.endertainment.quartzdefenders.events;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.projectiles.ProjectileSource;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.ColorFormat;

public class ProjectileDamageListener implements Listener {

    public ProjectileDamageListener(QuartzDefenders plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    //Displaying damage
    @EventHandler
    public void onShootBow(EntityDamageByEntityEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof Player && event.getCause().equals(EntityDamageEvent.DamageCause.PROJECTILE)) {
            Entity damager = event.getDamager();
            if (damager instanceof Arrow) {
                Arrow arrow = (Arrow) damager;
                Player p = (Player) event.getEntity();
                ProjectileSource shoot = arrow.getShooter();
                if (!(shoot instanceof Player)) {
                    return;
                }
                if (p instanceof Player) {
                    Player h = (Player) entity;
                    double damage = event.getFinalDamage();
                    double hp = h.getHealth() - damage;
                    hp = hp < 0 ? 0 : hp;
                    Player sh = (Player) shoot;
                    sh.spigot().sendMessage(ChatMessageType.ACTION_BAR,  TextComponent.fromLegacyText(new ColorFormat(h.getDisplayName() + " &7\u27A0 &4" + "&4\u2764 &6 " + String.format("%.1f", hp) + "&7(&e" + String.format("%.1f", damage) + "&7)").format()));
                }
            }
        }
    }
    
}
