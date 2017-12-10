package ua.Endertainment.QuartzDefenders.Utils;

import org.bukkit.Location;
import org.bukkit.World;

public class Cuboid {
	
	private final World world;
	private final int x1;
	private final int y1;
	private final int z1;
	private final int x2;
	private final int y2;
	private final int z2;
	
	public Cuboid(Location loc1, Location loc2) {
		if(loc1.getWorld() != loc2.getWorld()) {
			throw new IllegalArgumentException("Locations must be on the same world");
		}
		Location l1, l2;
		l1 = new Location(loc1.getWorld(), loc1.getBlockX(), loc1.getBlockY(), loc1.getBlockZ()).add(2, 2, 2);
		l2 = new Location(loc2.getWorld(), loc2.getBlockX(), loc2.getBlockY(), loc2.getBlockZ()).subtract(2, 2, 2);
		this.world = l1.getWorld();
	    this.x1 = Math.min(l1.getBlockX(), l2.getBlockX());
	    this.y1 = Math.min(l1.getBlockY(), l2.getBlockY());
	    this.z1 = Math.min(l1.getBlockZ(), l2.getBlockZ());
	    this.x2 = Math.max(l1.getBlockX(), l2.getBlockX());
	    this.y2 = Math.max(l1.getBlockY(), l2.getBlockY());
	    this.z2 = Math.max(l1.getBlockZ(), l2.getBlockZ());
		
	}
	
	public boolean contains(int x, int y, int z) {
		return (x >= this.x1) && (x <= this.x2) && (y >= this.y1) && (y <= this.y2) && (z >= this.z1) && (z <= this.z2);
	}
	public boolean contains(Location loc) {
		if(loc.getWorld() != world) return false;
		return contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
	}
}
