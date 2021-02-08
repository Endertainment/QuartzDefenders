package ua.endertainment.quartzdefenders.events;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.utils.ColorFormat;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.Symbols;

public class DeathMessages implements Listener {

    private QuartzDefenders plugin;
    private final String pref = "&8"+Symbols.RIGHT_QUOTE+" ";
    
    public DeathMessages(QuartzDefenders plugin) {
        this.plugin = plugin;
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player killer = p.getKiller();
        EntityDamageEvent ldc = p.getLastDamageCause();
        if(ldc==null) return;
        EntityDamageEvent.DamageCause cause = ldc.getCause();

        Game game = plugin.getGame(p);

        if (game != null) {
            if (killer == null || killer.getName().equals(p.getName())) {
                e.setDeathMessage("");
                switch (cause) {
                    case ENTITY_EXPLOSION:
                        game.broadcastMessage(pref + Language.getString("death.blown_up", p.getDisplayName()));
                        break;
                    case STARVATION:
                        game.broadcastMessage(pref + Language.getString("death.hunger", p.getDisplayName()));
                        break;
                    case FALL:
                        game.broadcastMessage(pref + Language.getString("death.fall_self", p.getDisplayName()));
                        break;
                    case FIRE:
                        game.broadcastMessage(pref + Language.getString("death.fire_self", p.getDisplayName()));
                        break;
                    case HOT_FLOOR:
                        game.broadcastMessage(pref + Language.getString("death.magma_self", p.getDisplayName()));
                        break;
                    case LAVA:
                        game.broadcastMessage(pref + Language.getString("death.lava_self", p.getDisplayName()));
                        break;
                    case MAGIC:
                        game.broadcastMessage(pref + Language.getString("death.magic_self", p.getDisplayName()));
                        break;
                    case VOID:
                        game.broadcastMessage(pref + Language.getString("death.void_self", p.getDisplayName()));
                        break;
                    case POISON:
                        game.broadcastMessage(pref + Language.getString("death.poison_self", p.getDisplayName()));
                        break;
                    case ENTITY_ATTACK:
                        if (ldc instanceof EntityDamageByEntityEvent) {
                            EntityDamageByEntityEvent ev = (EntityDamageByEntityEvent) ldc;
                            String name = ev.getDamager().getCustomName() == null ? ev.getDamager().getName() : ev.getDamager().getCustomName();
                            game.broadcastMessage(pref + Language.getString("death.killed", p.getDisplayName(), name));
                        }
                        break;
                    default:
                        game.broadcastMessage(pref + Language.getString("death.self", p.getDisplayName()));
                }
            }
            if (killer instanceof Player) {
                e.setDeathMessage("");
                switch (cause) {
                    case ENTITY_EXPLOSION:
                        game.broadcastMessage(pref + Language.getString("death.blown_up_by", p.getDisplayName(), killer.getDisplayName()));
                        break;
                    case FALL:
                        game.broadcastMessage(pref + Language.getString("death.fall", p.getDisplayName(), killer.getDisplayName()));
                        break;
                    case FIRE:
                        game.broadcastMessage(pref + Language.getString("death.fire", p.getDisplayName(), killer.getDisplayName()));
                        break;
                    case FIRE_TICK:
                        game.broadcastMessage(pref + Language.getString("death.fire_aspect", p.getDisplayName(), killer.getDisplayName()));
                        break;
                    case HOT_FLOOR:
                        game.broadcastMessage(pref + Language.getString("death.magma", p.getDisplayName(), killer.getDisplayName()));
                        break;
                    case LAVA:
                        game.broadcastMessage(pref + Language.getString("death.lava", p.getDisplayName(), killer.getDisplayName()));
                        break;
                    case MAGIC:
                        game.broadcastMessage(pref + Language.getString("death.magic", p.getDisplayName(), killer.getDisplayName()));
                        break;
                    case POISON:
                        game.broadcastMessage(pref + Language.getString("death.poison", p.getDisplayName(), killer.getDisplayName()));
                        break;
                    case PROJECTILE:
                        game.broadcastMessage(pref + Language.getString("death.projectile", p.getDisplayName(), killer.getDisplayName()));
                        break;
                    case VOID:
                        game.broadcastMessage(pref + Language.getString("death.void", p.getDisplayName(), killer.getDisplayName()));
                        break;
                    default:
                        game.broadcastMessage(pref + Language.getString("death.killed", p.getDisplayName(), killer.getDisplayName()));
                }
            }
            return;
        }

        if (killer == null) {
            e.setDeathMessage(new ColorFormat(pref + Language.getString("death.self", p.getDisplayName())).format());
        }
        if (killer != null && killer instanceof Player) {
            e.setDeathMessage(new ColorFormat(pref + Language.getString("death.killed", p.getDisplayName(), killer.getDisplayName())).format());
        }
    }

}
