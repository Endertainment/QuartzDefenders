package ua.Coolboy.QuartzDefenders.Turrets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;
import ua.Endertainment.QuartzDefenders.Game;
import ua.Endertainment.QuartzDefenders.GameTeam;
import ua.Endertainment.QuartzDefenders.QuartzDefenders;

public class Turret {
    private QuartzDefenders plugin;
    private List<Entity> stands = new ArrayList<>();
    private Game game;
    private GameTeam team;
   
    public Turret(Game game, GameTeam team) {
        this.game = game;
        this.team = team;
    }
    
    public List<Entity> getStands() {
        return stands;
    }
    
    public ArmorStand addStand(Location loc, GameTeam team) {
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
                team.addTurret(turret);
                stands.add(turret);
                return turret;
    }
 
    public Game getGame() {
        return game;
    }
 
    public GameTeam getTeam() {
        return team;
    }
 
   
   
}