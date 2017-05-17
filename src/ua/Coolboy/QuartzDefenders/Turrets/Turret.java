package ua.Coolboy.QuartzDefenders.Turrets;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.SpectralArrow;
import org.bukkit.entity.TippedArrow;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;
import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class Turret {

    private QuartzDefenders plugin;
    private static List<Entity> stands = new ArrayList<>();
    private final Game game;
    private final Player owner;
    private final GameTeam team;
    private final ArmorStand stand;

    public Turret(Player owner, Location loc, QuartzDefenders plugin) {
        this.plugin = plugin;
        this.owner = owner;
        this.game = plugin.getGame(owner);

        this.team = game.getTeam(owner);
        ArmorStand turret = (ArmorStand) loc.getWorld().spawnEntity(loc.add(0.5, 1, 0.5), EntityType.ARMOR_STAND);
        turret.getEquipment().setHelmet(new ItemStack(Material.DISPENSER, 1));
        turret.setSmall(true);
        turret.setInvulnerable(true);
        turret.setCustomName("Турель");
        turret.setGravity(false);
        turret.setCustomNameVisible(true);
        turret.setMetadata("turret", new FixedMetadataValue(plugin, true));
        turret.setMetadata("lived", new FixedMetadataValue(plugin, 0));
        turret.setMetadata("type", new FixedMetadataValue(plugin, "normal"));
        //team.addTurret(turret);
        stands.add(turret);
        this.stand = turret;
    }

    public static List<Entity> getStands() {
        return stands;
    }

    public ArmorStand getStand() {
        return stand;
    }

    public void remove() {
        stands.remove(stand);
    }

    public void shoot(Vector vector) {
        switch (getStand().getMetadata("type").get(0).asString()) {
            case "fire":
                launchArrow(vector).setFireTicks(5000);
                break;
            case "slow":
                launchTippedArrow(vector, new PotionEffect(PotionEffectType.SLOW, 200, 0));
                break;
            case "poison":
                launchTippedArrow(vector, new PotionEffect(PotionEffectType.POISON, 20, 0));
                break;
            case "glow":
                SpectralArrow spec = getStand().launchProjectile(SpectralArrow.class);
                spec.setVelocity(vector);
                spec.spigot().setDamage(2);
                spec.setCritical(false);
                break;
            default:
                launchArrow(vector);
                break;
        }
    }

    public void lookAtTarget(Entity mob) {
        double dX = getStand().getLocation().getX() - mob.getLocation().getX();
        double dZ = getStand().getLocation().getZ() - mob.getLocation().getZ();
        double yaw = Math.atan2(dZ, dX) + Math.PI / 2;
        getStand().setHeadPose(new EulerAngle(0, yaw, 0));
    }

    public Arrow launchArrow(Vector vector) {
        Arrow arrow = stand.launchProjectile(Arrow.class);
        arrow.setVelocity(vector.multiply(0.4));
        arrow.setCritical(false);
        arrow.spigot().setDamage(2);
        return arrow;
    }

    public TippedArrow launchTippedArrow(Vector vector, PotionEffect effect) {
        TippedArrow arrow = stand.launchProjectile(TippedArrow.class);
        arrow.setVelocity(vector.multiply(0.4));
        arrow.setCritical(false);
        arrow.spigot().setDamage(2);
        arrow.addCustomEffect(effect, true);
        return arrow;
    }

    public Game getGame() {
        return game;
    }

    public GameTeam getTeam() {
        return team;
    }
}
