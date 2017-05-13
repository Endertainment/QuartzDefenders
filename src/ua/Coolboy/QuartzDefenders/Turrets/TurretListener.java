package ua.Coolboy.QuartzDefenders.Turrets;

import java.util.Collection;
import java.util.List;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;
import ua.Endertainment.QuartzDefenders.Utils.FilesUtil;

public class TurretListener implements Listener {
    QuartzDefenders plugin;
    int liveTime;
    
    public TurretListener(QuartzDefenders plugin) {
        this.plugin = plugin;
        this.liveTime = new FilesUtil(plugin).getGameInfo().getInt("turret_livetime");
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }
    
    @EventHandler
    public void interactEvent(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        boolean nearStand = false;
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)
                && event.getClickedBlock().getType().equals(Material.STRUCTURE_BLOCK)
                && event.hasItem()
                && event.getItem().getType().equals(Material.IRON_BARDING)
                && event.getItem().getItemMeta().getLore().contains("Створює турель")) {
            if (!player.getGameMode().equals(GameMode.CREATIVE)) {
                if (player.getInventory().getItemInMainHand().getAmount() > 1) {
                    player.getInventory().getItemInMainHand().setAmount(player.getInventory().getItemInMainHand().getAmount() - 1);
                } else {
                    player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                }
            }
            Block block = event.getClickedBlock();
            Location location = block.getLocation();
            Collection<Entity> near = block.getLocation().add(0, 1, 0).getWorld().getNearbyEntities(block.getLocation(), 2, 2, 2);
            for (Entity st : near) {
                if (st instanceof ArmorStand && st.getName().startsWith("Турель")) {
                    nearStand = true;
                }
            }
            if (!nearStand) {
                Turret turret = new Turret(player, location, plugin);
                runTurret(turret, player);
            }
        }
    }
    
    public void runTurret(Turret stand, Player player) {
        ArmorStand turret = stand.getStand();
        new BukkitRunnable() {
            @Override
            public void run() {

                if (turret.isDead()) {
                    turret.remove();
                    this.cancel();
                }
                List<Entity> list = turret.getNearbyEntities(7, 5, 7);
                for (Entity mob : list) {
                    if (mob.getLocation().distance(turret.getLocation()) < 1.2) {
                        Location tploc = mob.getLocation().add(0.7, 0, 0.7);
                        mob.teleport(tploc);
                    }
                    if (mob instanceof Player) {
                        Player playery = (Player) mob;
                        if (!mob.getName().equals(player.getName())
                                && !playery.getGameMode().equals(GameMode.SPECTATOR)
                                && !playery.getGameMode().equals(GameMode.CREATIVE)
                                && !playery.getScoreboard().getTeam(turret.getMetadata("team").get(0).asString()).hasEntry(playery.getName())) {
                            Vector vector = mob.getLocation().toVector().subtract(turret.getLocation().toVector());

                            switch (turret.getMetadata("type").get(0).asString()) {
                                case "fire":
                                    stand.launchArrow(vector).setFireTicks(5000);
                                    break;
                                case "slow":
                                    stand.launchTippedArrow(vector, new PotionEffect(PotionEffectType.SLOW, 200, 0));
                                    break;
                                case "poison":
                                    stand.launchTippedArrow(vector, new PotionEffect(PotionEffectType.POISON, 20, 0));
                                    break;
                                case "glow":
                                    SpectralArrow spec = turret.launchProjectile(SpectralArrow.class);
                                    spec.setVelocity(vector);
                                    spec.spigot().setDamage(2);
                                    spec.setCritical(false);
                                    break;
                                default:
                                    stand.launchArrow(vector);
                                    break;
                            }

                            double dX = turret.getLocation().getX() - mob.getLocation().getX();
                            double dZ = turret.getLocation().getZ() - mob.getLocation().getZ();
                            double yaw = Math.atan2(dZ, dX) + Math.PI / 2;
                            turret.setHeadPose(new EulerAngle(0, yaw, 0));
                            break;
                        }
                    } else if (mob instanceof LivingEntity) {
                        Vector vector = mob.getLocation().toVector().subtract(turret.getLocation().toVector());
                        switch (turret.getMetadata("type").get(0).asString()) {
                            case "fire":
                                stand.launchArrow(vector).setFireTicks(5000);
                                break;
                            case "slow":
                                stand.launchTippedArrow(vector, new PotionEffect(PotionEffectType.SLOW, 200, 0));
                                break;
                            case "poison":
                                stand.launchTippedArrow(vector, new PotionEffect(PotionEffectType.POISON, 20, 0));
                                break;
                            case "glow":
                                SpectralArrow spec = turret.launchProjectile(SpectralArrow.class);
                                spec.setVelocity(vector);
                                spec.spigot().setDamage(2);
                                spec.setCritical(false);
                                break;
                            default:
                                stand.launchArrow(vector);
                                break;
                        }
                        double dX = turret.getLocation().getX() - mob.getLocation().getX();
                        double dZ = turret.getLocation().getZ() - mob.getLocation().getZ();
                        double yaw = Math.atan2(dZ, dX) + Math.PI / 2;
                        turret.setHeadPose(new EulerAngle(0, yaw, 0));
                        break;
                    }
                }
                turret.setMetadata("lived", new FixedMetadataValue(plugin, turret.getMetadata("lived").get(0).asInt() + 40));
                turret.setCustomName("Турель " + Integer.toString((liveTime - turret.getMetadata("lived").get(0).asInt()) / 20) + "с");
                if (turret.getMetadata("lived").get(0).asInt() > liveTime) {
                    turret.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_NORMAL, turret.getLocation(), 300);
                    turret.getLocation().getWorld().playSound(turret.getLocation(), Sound.ENTITY_BLAZE_HURT, 50, 0);
                    turret.getLocation().getWorld().createExplosion(turret.getLocation(), 2.0F);
                    turret.remove();
                    stand.remove();
                    this.cancel();
                }
            }
        }.runTaskTimer(plugin, 0, 40);
    }
}
