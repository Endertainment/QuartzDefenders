package ua.endertainment.quartzdefenders.game;

import java.util.ArrayList;
import java.util.List;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

public class Ores {
    
    Game game;
    List<OreMaterial> materials;
    
    public Ores(Game game, ConfigurationSection section) {
        this.game = game;
        this.materials = new ArrayList<>();
        for (String s : section.getKeys(false)) {
            Material blockMaterial = Material.valueOf(s);
            List<Location> locs = new ArrayList<>();
            for (String loc : section.getStringList(s + ".list")) {
                int x, y, z;
                String[] array = loc.split(",");
                x = Integer.parseInt(array[0]);
                y = Integer.parseInt(array[1]);
                z = Integer.parseInt(array[2]);
                locs.add(new Location(game.getGameWorld(), x, y, z));
            }
            int time = section.getInt(s + ".regenerate_time");
            materials.add(new OreMaterial(blockMaterial, locs, time));
        }
    }
    
    public List<OreMaterial> getMaterials() {
        return materials;
    }
    
    public int getRegenerateTime(Material material) {
        for(OreMaterial mat : materials) {
            if(mat.getMaterial().equals(material)) return mat.getRegenerationTime();
        }
        return 200;
    }
    
    public boolean isRegenerativeMaterial(Material material) {
        for(OreMaterial mat : materials) {
            if(mat.getMaterial().equals(material)) return true;
        }
        return false;
    }
    
    public boolean isOre(Location location) {
        if(!isRegenerativeMaterial(location.getBlock().getType())) return false;
        for(OreMaterial mat : materials) {
            for(Location loc : mat.getLocations()) {
                if(loc.equals(location)) return true;
            }
        }
        return false;
    }
    
    public OreMaterial getByMaterial(Material material) {
        for(OreMaterial mat : materials) {
            if(mat.getMaterial().equals(material)) return mat;
        }
        return null;
    }
    
    public OreMaterial getByLocation(Location location) {
        for(OreMaterial mat : materials) {
            for(Location loc : mat.getLocations()) {
                if(loc.equals(location)) return mat;
            }
        }
        return null;
    }
    
    public class OreMaterial {

        Material material;
        List<Location> locations;
        int regenTime;

        private OreMaterial(Material material, List<Location> locations, int regenTime) {
            this.material = material;
            this.locations = locations;
            this.regenTime = regenTime;
        }
        
        public Material getMaterial() {
            return material;
        }
        
        public List<Location> getLocations() {
            return locations;
        }
        
        public int getRegenerationTime() {
            return regenTime;
        }
    }
}
