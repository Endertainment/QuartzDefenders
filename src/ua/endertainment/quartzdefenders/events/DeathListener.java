package ua.endertainment.quartzdefenders.events;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import ua.endertainment.quartzdefenders.game.Game;
import ua.endertainment.quartzdefenders.game.GameTeam;
import ua.endertainment.quartzdefenders.QuartzDefenders;
import ua.endertainment.quartzdefenders.stats.StatsPlayer;
import ua.endertainment.quartzdefenders.utils.Language;
import ua.endertainment.quartzdefenders.utils.LoggerUtil;
import ua.endertainment.quartzdefenders.utils.Replacer;

public class DeathListener implements Listener {

    private QuartzDefenders plugin;
    private Set<Player> freeze;

    public DeathListener(QuartzDefenders plugin) {
        this.plugin = plugin;
        this.freeze = new HashSet<>();
        Bukkit.getPluginManager().registerEvents(this, this.plugin);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onDeath(PlayerDeathEvent e) {
        Player p = e.getEntity();
        Player killer = p.getKiller();
        if (plugin.getGame(p) == null) {
            return;
        }

        Game game = plugin.getGame(p);
        int resp = game.getPlayersRespawnTime();

        if (game.getTeam(p) == null) {
            p.setHealth(20);
            p.setGameMode(GameMode.SPECTATOR);
            p.teleport(game.getMapCenter());
            return;
        }

        GameTeam team = game.getTeam(p);

        if (killer != null && killer instanceof Player) {
            game.getKillsStats().addKill(plugin.getGamePlayer(killer));

            StatsPlayer sp = new StatsPlayer(killer);
            sp.addKill();

            killer.getInventory().addItem(new ItemStack(Material.EMERALD));
            killer.getInventory().addItem(new ItemStack(Material.BONE));
            killer.getInventory().addItem(new ItemStack(Material.RAW_BEEF));

            game.getComboManager().start(plugin.getGamePlayer(killer), game);

        }

        game.getSidebar().refresh();

        StatsPlayer sp = new StatsPlayer(p);
        sp.addDeath();

        if (team.canRespawn()) {
            p.setHealth(20);
            p.setFoodLevel(20);
            p.setExp(0);
            p.setLevel(0);
            p.setGameMode(GameMode.SPECTATOR);
            p.sendMessage(LoggerUtil.gameMessage(Language.getString("game.game"), Language.getString("game.respawn", new Replacer("{0}", resp + ""))));
            freeze.add(p);
            if (p.getLocation().getY() <= 0) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    p.teleport(new Location(p.getWorld(), p.getLocation().getX(), 80, p.getLocation().getZ()));
                });
            }
            BukkitRunnable run = new BukkitRunnable() {
                @Override
                public void run() {
                    p.setHealth(20);
                    p.setFoodLevel(20);
                    p.setGameMode(GameMode.SURVIVAL);
                    p.teleport(team.getSpawnLocation());
                    p.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, 5 * 20, 200));
                    p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 5 * 20, 24));
                    freeze.remove(p);
                }
            };
            run.runTaskLater(plugin, (resp * 20));
        } else {
            team.removePlayer(plugin.getGamePlayer(p), false);
            p.setHealth(20);
            p.setGameMode(GameMode.SPECTATOR);
            game.getSpectators().add(plugin.getGamePlayer(p));

            if (p.getLocation().getY() <= 0) {
                Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    p.teleport(new Location(p.getWorld(), p.getLocation().getX(), 80, p.getLocation().getZ()));
                });
            }

        }

    }

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        if (freeze.isEmpty()) {
            return;
        }
        if (freeze.contains(e.getPlayer())) {
            Location f = e.getFrom();
            Location t = e.getTo();
            if (f.getX() != t.getX() || f.getY() != t.getY() || f.getZ() != t.getZ()) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e) {
        Player p = e.getPlayer();
        if (plugin.getGame(p) != null) {
            if (e.getCause().equals(TeleportCause.SPECTATE)) {
                e.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (plugin.getGame(p) == null) {
            return;
        }

        if (!p.getGameMode().equals(GameMode.SPECTATOR)) {
            return;
        }

        if (e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {

            if (e.getClickedBlock().getType().equals(Material.BURNING_FURNACE)
                    || e.getClickedBlock().getType().equals(Material.TRAPPED_CHEST)
                    || e.getClickedBlock().getType().equals(Material.CHEST)
                    || e.getClickedBlock().getType().equals(Material.FURNACE)
                    || e.getClickedBlock().getType().equals(Material.BREWING_STAND)) {
                e.setCancelled(true);
            }

        }
    }

}
